# RePlate — Recipe Evaluation Cases

These cases assess whether AI-generated recipes satisfy
the requested adaptation goal.

Structural validity is tested separately with JUnit.

## Case 1 — Vegan creamy chicken pasta

Goal: VEGAN

Original recipe:
- 200 g pasta
- 150 g chicken breast
- 100 ml dairy cream
- Cook pasta, fry chicken, add cream.

Expected behavior:
- Replace chicken with a suitable plant-based protein.
- Replace dairy cream with a plant-based alternative.
- Provide realistic quantities and cooking instructions.
- Address uncertainty about pasta ingredients if relevant.

## Case 2 — Vegan pancakes

Goal: VEGAN

Original recipe:
- 200 g wheat flour
- 2 eggs
- 250 ml cow's milk
- 20 g honey
- Mix ingredients and fry pancakes.

Expected behavior:
- Replace eggs, milk, and honey.
- Preserve a workable pancake batter.
- Explain meaningful substitutions.
- Provide practical cooking instructions.

## Case 3 — Calorie-conscious creamy pasta

Goal: CALORIE_CONSCIOUS

Original recipe:
- 200 g pasta
- 150 g chicken breast
- 200 ml heavy cream
- 50 g Parmesan
- Cook pasta, fry chicken, prepare creamy sauce.

Expected behavior:
- Suggest practical lower-calorie substitutions.
- Preserve a satisfying meal and protein source.
- Avoid relying only on much smaller portions.
- Avoid unsupported numerical calorie claims.

## Case 4 — Calorie-conscious fried potatoes

Goal: CALORIE_CONSCIOUS

Original recipe:
- 500 g potatoes
- 60 g butter
- 100 g mayonnaise
- Fry potatoes in butter and serve with mayonnaise.

Expected behavior:
- Consider a lower-fat cooking method.
- Suggest a lighter sauce or dip.
- Preserve the character of the dish.
- Avoid unsupported numerical calorie claims.

## Manual evaluation checklist

For each generated recipe, verify:

- [ ] The requested goal is satisfied.
- [ ] Ingredient substitutions are practical.
- [ ] Quantities are realistic.
- [ ] Instructions are clear and usable.
- [ ] The original dish remains recognizable.
- [ ] Changes explain meaningful adaptations.
- [ ] Warnings identify important uncertainties.
- [ ] No unsupported nutrition claims are made.

Note: Passing structural tests does not guarantee
that an AI-generated recipe is correct or safe.
