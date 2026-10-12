export interface Account {
  id?: string;
  createdAt?: string | Date;
  balance?: number;
  type?: string;
  customerId?: string;
}

export enum RequestStatus {
  LOADING = "LOADING",
  LOADED = "LOADED",
  ERROR = "ERROR",
}

export interface AccountListState {
  accounts?: Account[];
  status?: RequestStatus;
  errorMessage?: string;
}
