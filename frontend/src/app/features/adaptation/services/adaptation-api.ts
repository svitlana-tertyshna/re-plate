import { HttpClient } from "@angular/common/http";
import { inject, Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { AdaptationRequest } from "../models/adaptation-request";
import { AdaptationResult } from "../models/adaptation-result";

@Injectable({
  providedIn: 'root',
})
export class AdaptationApi {
  private readonly http = inject(HttpClient);

  createAdaptation(
    request: AdaptationRequest
  ): Observable<AdaptationResult> {
    return this.http.post<AdaptationResult>(
      '/api/v1/adaptations',
      request
    );
  }
}
