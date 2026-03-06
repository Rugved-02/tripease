import { inject, PLATFORM_ID } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { AuthService } from '../../services/auth/auth-service';
import { isPlatformBrowser } from '@angular/common';

export const authGuard: CanActivateFn = (route, state) => {

  const platformId = inject(PLATFORM_ID);
  const authService = inject(AuthService);
  const router = inject(Router);

  // if (!isPlatformBrowser(platformId)) {
  //   return true; 
  // }

  if (authService.isLoggedIn()) {
    return true; // Allow access
  } else {
    // return router.parseUrl('/login'); // Redirect to login
    router.navigate(['/login']);
    return false;
  }
};
