import { HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { finalize } from 'rxjs';
import { Loading } from '../services/loading';

export const loadingInterceptorInterceptor: HttpInterceptorFn = (req, next) => {
  const loadingService = inject(Loading);
  loadingService.setLoading();

  return next(req).pipe(finalize(() => loadingService.setUnloading(false)));
};
