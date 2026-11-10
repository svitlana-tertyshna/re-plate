
import { TestBed } from '@angular/core/testing';
import { provideRouter } from '@angular/router';
import { of, Subject } from 'rxjs';
import { beforeEach, describe, expect, it, vi } from 'vitest';
import type { AdaptationResult } from './features/adaptation/models/adaptation-result';

import { App } from './app';
import { AdaptationApi } from './features/adaptation/services/adaptation-api';


describe('Recipe form validation', () => {

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [App],
      providers: [
        provideRouter([]),
        {
          provide: AdaptationApi,
          useValue: {
            createAdaptation: () => {
              throw new Error('Unexpected API request');
            },
          },
        },
      ],
    });
  });


  it('rejects whitespace-only recipes', async () => {
    const fixture = TestBed.createComponent(App);
    await fixture.whenStable();

    const textarea = fixture.nativeElement.querySelector(
      '#recipe-text'
    ) as HTMLTextAreaElement;

    textarea.value = ' '.repeat(20);
    textarea.dispatchEvent(new Event('input'));

    await fixture.whenStable();

    expect(fixture.nativeElement.textContent).toContain(
      'Please enter a recipe, not just spaces.'
    );
  });

  it('accepts a valid recipe', async () => {
    const fixture = TestBed.createComponent(App);

    await fixture.whenStable();

    const textarea = fixture.nativeElement.querySelector(
      '#recipe-text'
    ) as HTMLTextAreaElement;

    textarea.value = 'Pasta with chicken and cream';
    textarea.dispatchEvent(new Event('input'));

    await fixture.whenStable();

    const pageText = fixture.nativeElement.textContent as string;

    expect(pageText).toContain(
      'Your recipe meets the input requirements.'
    );

    expect(pageText).not.toContain(
      'Please enter at least 15 characters.'
    );
  });


  it('submits a valid recipe to the API service', async () => {
    const fakeResult: AdaptationResult = {
      id: 'test-id-123',
      goal: 'VEGAN',
      recipe: {
        title: 'Demo Vegan Pasta',
        ingredients: [],
        instructions: [],
        changes: [],
        warnings: [],
      },
    };

    const api = TestBed.inject(AdaptationApi);

    const apiSpy = vi.spyOn(api, 'createAdaptation')
      .mockReturnValue(of(fakeResult));

    const fixture = TestBed.createComponent(App);
    await fixture.whenStable();

    const textarea = fixture.nativeElement.querySelector(
      '#recipe-text'
    ) as HTMLTextAreaElement;

    textarea.value = 'Pasta with chicken and cream';
    textarea.dispatchEvent(new Event('input'));

    fixture.detectChanges();
    await fixture.whenStable();

    const submitButton = fixture.nativeElement.querySelector(
      'button[type="submit"]'
    ) as HTMLButtonElement;

    submitButton.click();
    await fixture.whenStable();

    expect(apiSpy).toHaveBeenCalledTimes(1);

    expect(apiSpy).toHaveBeenCalledWith({
      recipeText: 'Pasta with chicken and cream',
      goal: 'VEGAN',
    });
  });

  it('does not submit an invalid recipe', async () => {
    const api = TestBed.inject(AdaptationApi);
    const apiSpy = vi.spyOn(api, 'createAdaptation');

    const fixture = TestBed.createComponent(App);
    await fixture.whenStable();

    const textarea = fixture.nativeElement.querySelector(
      '#recipe-text'
    ) as HTMLTextAreaElement;

    textarea.value = 'Pasta';
    textarea.dispatchEvent(new Event('input'));

    fixture.detectChanges();
    await fixture.whenStable();

    const submitButton = fixture.nativeElement.querySelector(
      'button[type="submit"]'
    ) as HTMLButtonElement;

    submitButton.click();

    fixture.detectChanges();
    await fixture.whenStable();

    expect(apiSpy).not.toHaveBeenCalled();

    expect(fixture.nativeElement.textContent).toContain(
      'Please enter at least 15 characters.'
    );
  });

  it('shows loading state while waiting for the API', async () => {
    const pendingResponse = new Subject<AdaptationResult>();

    const api = TestBed.inject(AdaptationApi);
    const apiSpy = vi.spyOn(api, 'createAdaptation')
      .mockReturnValue(pendingResponse.asObservable());

    const fixture = TestBed.createComponent(App);
    await fixture.whenStable();

    const textarea = fixture.nativeElement.querySelector(
      '#recipe-text'
    ) as HTMLTextAreaElement;

    textarea.value = 'Pasta with chicken and cream';
    textarea.dispatchEvent(new Event('input'));

    fixture.detectChanges();
    await fixture.whenStable();

    const submitButton = fixture.nativeElement.querySelector(
      'button[type="submit"]'
    ) as HTMLButtonElement;

    submitButton.click();
    fixture.detectChanges();
    await fixture.whenStable();

    expect(apiSpy).toHaveBeenCalledTimes(1);
    expect(submitButton.disabled).toBe(true);
    expect(submitButton.textContent).toContain(
      'Adapting your recipe...'
    );

    const fakeResult: AdaptationResult = {
      id: 'test-id-456',
      goal: 'VEGAN',
      recipe: {
        title: 'Demo Vegan Pasta',
        ingredients: ['200 g egg-free pasta'],
        instructions: ['Cook the pasta.'],
        changes: [],
        warnings: [],
      },
    };

    pendingResponse.next(fakeResult);
    pendingResponse.complete();

    fixture.detectChanges();
    await fixture.whenStable();

    expect(submitButton.disabled).toBe(false);

    expect(submitButton.textContent).toContain(
      'Adapt my recipe'
    );
    expect(fixture.nativeElement.textContent).toContain('Demo Vegan Pasta');
  });

  it('shows an error when the API request fails', async () => {
    const pendingResponse = new Subject<AdaptationResult>();

    const api = TestBed.inject(AdaptationApi);
    vi.spyOn(api, 'createAdaptation')
      .mockReturnValue(pendingResponse.asObservable());

    const fixture = TestBed.createComponent(App);
    await fixture.whenStable();

    const textarea = fixture.nativeElement.querySelector(
      '#recipe-text'
    ) as HTMLTextAreaElement;

    textarea.value = 'Pasta with chicken and cream';
    textarea.dispatchEvent(new Event('input'));

    fixture.detectChanges();
    await fixture.whenStable();

    const submitButton = fixture.nativeElement.querySelector(
      'button[type="submit"]'
    ) as HTMLButtonElement;

    submitButton.click();

    fixture.detectChanges();
    await fixture.whenStable();

    expect(submitButton.disabled).toBe(true);

    // Simulate a failed backend request.
    pendingResponse.error(
      new Error('Simulated backend failure')
    );

    fixture.detectChanges();
    await fixture.whenStable();

    expect(submitButton.disabled).toBe(false);
    expect(fixture.nativeElement.textContent).toContain('We could not adapt your recipe. Please try again.')
    expect(fixture.nativeElement.textContent).not.toContain('Demo Vegan Pasta')
  });

});
