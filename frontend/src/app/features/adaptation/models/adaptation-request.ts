export type AdaptationGoal = 'VEGAN' | 'CALORIE_CONSCIOUS';

export interface AdaptationRequest {
  recipeText: string;
  goal: AdaptationGoal;
}
