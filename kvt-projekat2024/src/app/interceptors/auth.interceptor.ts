import { Injectable } from '@angular/core';
import { HttpInterceptor, HttpRequest, HttpHandler, HttpEvent } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable()
export class AuthInterceptor implements HttpInterceptor {

  
intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
  const token = localStorage.getItem('authToken');
  const nonAuthUrls = ['/auth/authenticate', '/account-requests', '/facilities', '/exercises/reserve', '/users/1/change-password'];

  // Proverite da li zahtev ide prema ruti koja ne zahteva token
  if (!nonAuthUrls.some(url => req.url.includes(url)) && token) {
      const authReq = req.clone({
          setHeaders: { Authorization: `Bearer ${token}` }
      });
      return next.handle(authReq);
  }
  return next.handle(req);
}
}