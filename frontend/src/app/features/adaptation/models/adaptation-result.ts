import { AdaptationGoal } from "./adaptation-request";

export interface GeneratedRecipe {
  title: string;
  ingredients: string[];
  instructions: string[];
  changes: string[];
  warnings: string[];
}

export interface AdaptationResult {
  id: string;
  goal: AdaptationGoal;
  recipe: GeneratedRecipe;
}
