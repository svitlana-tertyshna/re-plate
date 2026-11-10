import type { AdaptationRequest } from './features/adaptation/models/adaptation-request';
import type { AdaptationResult } from './features/adaptation/models/adaptation-result';
import { AdaptationApi } from './features/adaptation/services/adaptation-api';

import { Component, inject, signal } from '@angular/core';
import {
  FormControl,
  FormGroup,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { RouterOutlet } from '@angular/router';

@Component({
  imports: [RouterOutlet, ReactiveFormsModule],
  selector: 'app-root',
  templateUrl: './app.html',
})
export class App {
  private readonly adaptationApi = inject(AdaptationApi);
  protected readonly adaptationResult =
    signal<AdaptationResult | null>(null);
  protected readonly isLoading = signal(false);
  protected readonly errorMessage = signal<string | null>(null);

  protected readonly recipeForm = new FormGroup({
    recipeText: new FormControl('', {
      nonNullable: true,
      validators: [
        Validators.required,
        Validators.minLength(15),
        Validators.pattern(/\S/),
      ],
    }),

    goal: new FormControl<'VEGAN' | 'CALORIE_CONSCIOUS'>('VEGAN', {
      nonNullable: true,
    }),
  });

  protected submitRecipe(): void {
    this.recipeForm.markAllAsTouched();
    if (this.recipeForm.invalid) {
      return;
    }
    const adaptationRequest: AdaptationRequest = this.recipeForm.getRawValue();
    this.adaptationResult.set(null);
    this.errorMessage.set(null);
    this.isLoading.set(true);

    this.adaptationApi.createAdaptation(adaptationRequest).subscribe({
      next: (result) => {
        this.adaptationResult.set(result);
        this.isLoading.set(false);
      },
      error: (error) => {
        this.isLoading.set(false);
        this.errorMessage.set(
          'We could not adapt your recipe. Please try again.'
        );
        console.error('Adaptation request failed:', error);
      },
    });
  }

}
