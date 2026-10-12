import { Component, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { AsyncPipe, DatePipe } from '@angular/common';
import { catchError, map, of, startWith, Subject, switchMap } from 'rxjs';
import { RequestStatus } from '../model/account.model';
import type { Account, AccountListState } from '../model/account.model';
import { Loading } from '../services/loading';

@Component({
  selector: 'app-accounts',
  imports: [
    AsyncPipe,
    DatePipe
  ],
  templateUrl: './accounts.html',
  styleUrl: './accounts.css',
})
export class Accounts {
  private http = inject(HttpClient);
  public loadservice = inject(Loading);
  private reloadAccounts = new Subject<void>();

  accounts$ = this.reloadAccounts.pipe(
    startWith(undefined),
    switchMap(() =>
      this.http.get<Account[]>('http://localhost:9999/ebank-service/api/accounts').pipe(
        map(
          (accounts): AccountListState => ({
            accounts,
            status: RequestStatus.LOADED,
          }),
        ),
        catchError(
          (error: unknown) =>
            of<AccountListState>({
              accounts: [],
              status: RequestStatus.ERROR,
              errorMessage:
                error instanceof Error
                  ? error.message
                  : 'Impossible de charger les comptes.',
            }),
        ),
        startWith<AccountListState>({
          accounts: [],
          status: RequestStatus.LOADING,
        }),
      ),
    ),
  );

  loadAccounts(): void {
    this.reloadAccounts.next();
  }
}
