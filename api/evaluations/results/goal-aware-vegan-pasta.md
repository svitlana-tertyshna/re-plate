# RePlate — Goal-Aware Recipe Evaluation

## Test information

- **Date:** 2026-11-05
- **Evaluation case:** Case 1 — Vegan creamy chicken pasta
- **Adaptation goal:** VEGAN
- **Git commit:** `6a58900`
- **AI model:** gpt-4o-mini (configured)
- **Response JSON file:** `api/examples/adaptations/201-goal-aware-vegan-pasta.json`
- **Prompt version:** Goal-aware prompt using `RecipePromptBuilder`

## Evaluation criteria

Score each criterion:

- 0 = Failed
- 1 = Partially satisfied / uncertain
- 2 = Fully satisfied

| Criterion | Score (0–2) | Notes |
|---|---|---|
| Goal adherence | 1 | Animal-derived ingredients were replaced, but the recipe does not specify egg-free pasta. The packaged chicken substitute also requires verification. |
| Practical substitutions | 1 | Plant-based chicken substitute and vegan cream are plausible, but the protein replacement is not specific enough. |
| Realistic quantities | 2 | 150 g protein substitute and 100 ml cream are plausible quantities for 200 g pasta. |
| Clear cooking instructions | 1 | Steps are understandable but lack seasoning, cooking times, and product-specific guidance. |
| Original dish preserved | 2 | The dish remains a creamy pasta meal with a protein component. |
| Meaningful changes explained | 2 | Both major substitutions are explicitly described. |
| Appropriate warnings | 1 | Packaged ingredients are mentioned, but the possible egg content of pasta is overlooked. |
| No unsupported nutrition claims | 2 | No unverified calorie or nutritional values are provided. |

## Overall result

- **Total score:** 12/16
- **Goal adherence passed?** No — not fully verified
- **Critical issues:** Egg-free pasta is not specified. The plant-based chicken substitute is insufficiently defined. Neither is a confirmed non-vegan ingredient.
- **Final decision:** NEEDS REVIEW

## Observations

### What worked well?

- The generated recipe follows the requested adaptation goal in its main substitutions.
- The original creamy pasta dish remains recognizable.
- Ingredient quantities are plausible.
- Major substitutions are documented.
- The model avoids unsupported nutritional claims.

### What could be improved?

- Explicitly specify egg-free pasta.
- Prefer concrete plant-based protein ingredients instead of generic substitutes.
- Provide more detailed cooking instructions and seasoning guidance.
- Prioritize warnings about ingredients that could compromise vegan compliance.
- Avoid treating cruelty-free labeling as equivalent to vegan ingredient composition.

## Comparison with baseline

| Evaluation | Score | Decision |
|---|---|---|
| Original prompt — 2026-11-03 | 13/16 | NEEDS REVIEW |
| Goal-aware prompt — 2026-11-05 | 12/16 | NEEDS REVIEW |

The new recipe scored one point lower because its protein substitution was less specific.

Both responses still have unresolved vegan compliance questions concerning the pasta.

This comparison contains only one generation per prompt version. It does not establish which prompt performs better consistently.

## What should we test next?

1. Test Case 2 — Vegan pancakes, which requires multiple animal-derived ingredient substitutions.
2. Test a `CALORIE_CONSCIOUS` recipe to examine the second adaptation goal.
3. Consider refining prompt rules to encourage concrete ingredients, complete vegan checks, and more actionable cooking instructions.

Further OpenAI calls should be deliberate and recorded separately.
