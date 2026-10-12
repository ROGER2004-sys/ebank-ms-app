import { Component, inject } from '@angular/core';
import { AsyncPipe } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { MarkdownComponent } from 'ngx-markdown';
import { EMPTY, Observable, catchError, finalize } from 'rxjs';
import { Loading } from '../services/loading';

@Component({
  selector: 'app-bot-ui',
  imports: [AsyncPipe, FormsModule, MarkdownComponent],
  templateUrl: './bot-ui.html',
  styleUrl: './bot-ui.css',
})
export class BotUi {
  query = '';
  response$: Observable<string> | null = null;
  errorMessage: string | null = null;

  readonly loadservice = inject(Loading);

  askAgent(): void {
    const query = this.query.trim();
    if (!query || this.loadservice.isLoading()) {
      return;
    }

    this.response$ = null;
    this.errorMessage = null;
    this.loadservice.setLoading();
    const params = new URLSearchParams({ query });
    this.response$ = new Observable<string>((subscriber) => {
      const abortController = new AbortController();
      let responseText = '';

      const dispatchEvent = (event: string): void => {
        const data = event
          .split(/\r\n|\r|\n/)
          .filter((line) => line.startsWith('data:'))
          .map((line) => line.slice(5))
          .join('\n');
        if (data) {
          responseText += data;
          subscriber.next(responseText);
        }
      };

      void (async () => {
        try {
          const response = await fetch(
            `http://localhost:9999/ebank-bot/chatstream?${params.toString()}`,
            {
              headers: { Accept: 'text/event-stream' },
              signal: abortController.signal,
            },
          );
          if (!response.ok) {
            throw new Error(`Le bot a renvoyé une erreur (${response.status}).`);
          }
          if (!response.body) {
            throw new Error('Le flux du bot est vide.');
          }

          const reader = response.body.getReader();
          const decoder = new TextDecoder();
          let buffer = '';
          const processEvents = (): void => {
            const separator = /(?:\r\n|\r|\n){2}/;
            let boundary = separator.exec(buffer);
            while (boundary) {
              dispatchEvent(buffer.slice(0, boundary.index));
              buffer = buffer.slice(boundary.index + boundary[0].length);
              boundary = separator.exec(buffer);
            }
          };

          while (true) {
            const { done, value } = await reader.read();
            if (done) {
              break;
            }
            buffer += decoder.decode(value, { stream: true });
            processEvents();
          }
          buffer += decoder.decode();
          processEvents();
          if (buffer) {
            dispatchEvent(buffer);
          }
          subscriber.complete();
        } catch (error: unknown) {
          if (!subscriber.closed) {
            subscriber.error(error);
          }
        }
      })();

      return () => abortController.abort();
    }).pipe(
      catchError((error: unknown) => {
        this.errorMessage = error instanceof Error && error.message.startsWith('Le bot a renvoyé')
          ? error.message
          : 'Impossible de joindre le bot. Vérifiez que la Gateway et le service ebank-bot sont démarrés.';
        return EMPTY;
      }),
      finalize(() => this.loadservice.setUnloading(false)),
    );
  }
}
