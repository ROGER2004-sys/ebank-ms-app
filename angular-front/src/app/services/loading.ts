import { Injectable, signal } from '@angular/core';

@Injectable({
  providedIn: 'root',
})
export class Loading {
  loading = signal<boolean>(false);
  public isLoading = this.loading.asReadonly();

  public setLoading(): void {
    this.loading.set(true);
  }

  public setUnloading(value: boolean): void {
    this.loading.set(value);
  }
}
