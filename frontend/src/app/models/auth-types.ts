// src/app/models/auth-types.ts
export interface JwtClaims {
  sub: string;       // Subject (usually user ID)
  username: string;  // Explicitly add username
  exp: number;       // Expiration timestamp
  iat: number;       // Issued at timestamp
  roles?: string[];  // User roles
  // Add any other claims your JWT contains
}