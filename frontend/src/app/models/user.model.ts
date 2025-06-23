export interface User {
  username: string;
  token?: string;  // Only for login response
}

// For login credentials
export interface LoginCredentials {
  username: string;
  password: string;
}


