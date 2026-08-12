Absolutely. Given what oOh! told you — **"very simple problem", reasoning, <10 lines, TypeScript, no AI** — I would now stop learning syntax and do **7 timed problems**.

Don't look for fancy algorithms. These are deliberately the kind of small problems where your real-world engineering instincts should be enough.

## Round 1 — Warm-up

### Problem 1 — Filter + Map

Given:

```ts
const customers = [
    { cif: "C100", status: "ACTIVE" },
    { cif: "C200", status: "PENDING" },
    { cif: "C300", status: "ACTIVE" },
    { cif: "C400", status: "CLOSED" }
];
```

Return the CIFs of all `ACTIVE` customers.

Expected:

```text
["C100", "C300"]
```

Function:

```ts
function getActiveCifs(customers: ???): string[] {
    // ...
}
```

**Target time: 2 minutes**

---

# Problem 2 — Find

Given:

```ts
const customers = [
    { cif: "C100", name: "John" },
    { cif: "C200", name: "Sarah" },
    { cif: "C300", name: "Mike" }
];
```

Find the customer with CIF `"C200"`.

Return the customer object, or `undefined` if it doesn't exist.

Expected:

```ts
{ cif: "C200", name: "Sarah" }
```

Function:

```ts
function findCustomer(customers: ???, cif: string) {
    // ...
}
```

**Target time: 2 minutes**

---

# Problem 3 — Sum

Given:

```ts
const transactions = [
    { amount: 100, status: "SUCCESS" },
    { amount: 50, status: "FAILED" },
    { amount: 200, status: "SUCCESS" },
    { amount: 75, status: "SUCCESS" }
];
```

Return the total amount of successful transactions.

Expected:

```text
375
```

Function:

```ts
function successfulTotal(transactions: ???): number {
    // ...
}
```

**Target time: 3 minutes**

You've essentially already solved this one, so this should be quick.

---

# Problem 4 — Remove duplicates

Given:

```ts
const cifs = [
    "C100",
    "C200",
    "C100",
    "C300",
    "C200",
    "C400"
];
```

Return unique CIFs.

Expected:

```text
["C100", "C200", "C300", "C400"]
```

Function:

```ts
function uniqueCifs(cifs: string[]): string[] {
    // ...
}
```

**Target time: 2 minutes**

---

# Problem 5 — First duplicate

Given:

```ts
const ids = [
    "A",
    "B",
    "C",
    "D",
    "C",
    "B"
];
```

Return the **first duplicate encountered**.

Expected:

```text
"C"
```

If there is no duplicate:

```text
undefined
```

Function:

```ts
function firstDuplicate(ids: string[]): string | undefined {
    // ...
}
```

**Target time: 4 minutes**

This is where I want you to think about **Set vs Map**.

---

# Problem 6 — Group/count

This is probably the most useful one for you given the type of systems you work with.

Given:

```ts
const documents = [
    "PASSPORT",
    "UTILITY_BILL",
    "PASSPORT",
    "DRIVER_LICENCE",
    "PASSPORT",
    "UTILITY_BILL"
];
```

Return the number of times each document type occurs.

Expected:

```text
PASSPORT        3
UTILITY_BILL    2
DRIVER_LICENCE  1
```

You can return:

```ts
Map<string, number>
```

Function:

```ts
function countDocuments(documents: string[]): Map<string, number> {
    // ...
}
```

**Target time: 5 minutes**

You already wrote almost exactly this earlier.

---

# Problem 7 — The mock interview problem

This is the one I'd actually use as your final test.

You receive events:

```ts
const events = [
    { userId: "U1", status: "FAILED" },
    { userId: "U2", status: "SUCCESS" },
    { userId: "U1", status: "SUCCESS" },
    { userId: "U3", status: "FAILED" },
    { userId: "U2", status: "SUCCESS" },
    { userId: "U4", status: "SUCCESS" }
];
```

Return the IDs of users who have **at least one successful event**, without duplicates.

Expected:

```text
["U2", "U1", "U4"]
```

Function:

```ts
function successfulUsers(events: ???): string[] {
    // ...
}
```

**Target time: 5 minutes**

---

# 🎯 How I want you to practise these

Don't do what developers often do when they know the answer:

> "Oh, that's easy. `filter().map()`."

Instead, **talk yourself through the shape**.

For example:

### Problem 7

You should think:

> I only care about SUCCESS → `filter`.

Then:

> I need the userId → `map`.

Then:

> I don't want duplicates → `Set`.

So:

```text
filter → map → Set → array
```

That's the reasoning they're actually testing.

---

# ⏱️ Your 25-minute mock

Do these in order:

| Problem   |       Time |
| --------- | ---------: |
| 1         |      2 min |
| 2         |      2 min |
| 3         |      3 min |
| 4         |      2 min |
| 5         |      4 min |
| 6         |      5 min |
| 7         |      5 min |
| **Total** | **23 min** |

Then spend **5 minutes reviewing**.

Don't Google anything.

If you get stuck, **don't immediately ask me**. Give yourself the full time.

---

Yes. Let's step up to **10 medium problems**, but still keep them aligned with the oOh! interview: practical reasoning, TypeScript, small implementation, no LeetCode gymnastics.

**Don't look for the solution immediately.** For each one, first identify the data structure / array operation you need.

---

# 1. Highest transaction

Given:

```ts
const transactions = [
    { id: "T1", amount: 100 },
    { id: "T2", amount: 450 },
    { id: "T3", amount: 200 },
    { id: "T4", amount: 300 }
];
```

Return the transaction with the highest amount.

Expected:

```ts
{ id: "T2", amount: 450 }
```

```ts
function highestTransaction(transactions: ???) {
    // ...
}
```

**Think:** `reduce`?

---

# 2. Average successful transaction

```ts
const transactions = [
    { amount: 100, status: "SUCCESS" },
    { amount: 50, status: "FAILED" },
    { amount: 200, status: "SUCCESS" },
    { amount: 300, status: "SUCCESS" }
];
```

Return the average amount of successful transactions.

Expected:

```text
200
```

```ts
function averageSuccessful(transactions: ???): number {
    // ...
}
```

**Think:** filter + reduce.

---

# 3. Most frequent document

```ts
const documents = [
    "PASSPORT",
    "UTILITY_BILL",
    "PASSPORT",
    "DRIVER_LICENCE",
    "PASSPORT",
    "UTILITY_BILL"
];
```

Return the document type that appears most frequently.

Expected:

```text
"PASSPORT"
```

```ts
function mostFrequent(documents: string[]): string {
    // ...
}
```

**Think:** `Map` + counting.

---

# 4. Group customers by status

Given:

```ts
const customers = [
    { cif: "C100", status: "ACTIVE" },
    { cif: "C200", status: "PENDING" },
    { cif: "C300", status: "ACTIVE" },
    { cif: "C400", status: "CLOSED" },
    { cif: "C500", status: "ACTIVE" }
];
```

Return:

```ts
{
    ACTIVE: ["C100", "C300", "C500"],
    PENDING: ["C200"],
    CLOSED: ["C400"]
}
```

```ts
function groupByStatus(customers: ???) {
    // ...
}
```

**Think:** `Map` or object accumulator.

---

# 5. Find missing IDs

You have expected document IDs:

```ts
const expected = ["D1", "D2", "D3", "D4", "D5"];
```

But the system actually returned:

```ts
const received = ["D1", "D3", "D5"];
```

Return:

```text
["D2", "D4"]
```

```ts
function findMissing(expected: string[], received: string[]): string[] {
    // ...
}
```

**Think:** `Set` + `filter`.

---

# 6. Find users with multiple successful events

```ts
const events = [
    { userId: "U1", status: "SUCCESS" },
    { userId: "U2", status: "SUCCESS" },
    { userId: "U1", status: "SUCCESS" },
    { userId: "U3", status: "FAILED" },
    { userId: "U2", status: "FAILED" },
    { userId: "U1", status: "SUCCESS" }
];
```

Return users who have **more than one successful event**.

Expected:

```text
["U1"]
```

```ts
function usersWithMultipleSuccesses(events: ???): string[] {
    // ...
}
```

**Think:** this is a `Map` counting problem.

---

# 7. Merge records by ID

You receive customer information from two systems:

```ts
const customers = [
    { id: "C1", name: "John" },
    { id: "C2", name: "Sarah" }
];

const statuses = [
    { id: "C1", status: "ACTIVE" },
    { id: "C2", status: "PENDING" }
];
```

Return:

```ts
[
    { id: "C1", name: "John", status: "ACTIVE" },
    { id: "C2", name: "Sarah", status: "PENDING" }
]
```

```ts
function mergeCustomers(customers: ???, statuses: ???) {
    // ...
}
```

**Think:** `Map` is probably your friend.

This is a particularly useful one for you because it's very close to the **CIF → object ID → KYC** type of data joining you discuss in your system designs.

---

# 8. Find first non-duplicate

Given:

```ts
const ids = ["A", "B", "C", "B", "A", "D", "C"];
```

Return the **first ID that appears only once**.

Expected:

```text
"D"
```

```ts
function firstUnique(ids: string[]): string | undefined {
    // ...
}
```

**Think carefully.**

You probably need **two passes** or a `Map`.

Don't try to be clever.

---

# 9. Top two transactions

Given:

```ts
const transactions = [
    { id: "T1", amount: 100 },
    { id: "T2", amount: 500 },
    { id: "T3", amount: 250 },
    { id: "T4", amount: 400 }
];
```

Return the IDs of the two largest transactions.

Expected:

```text
["T2", "T4"]
```

```ts
function topTwo(transactions: ???): string[] {
    // ...
}
```

**Think:** You don't necessarily need to sort.

But sorting is also a perfectly reasonable answer unless the interviewer specifically asks for optimal complexity.

---

# 10. KYC processing problem 🔥

This one is designed specifically around your Silent KYC story.

You have:

```ts
const documents = [
    { objectId: "O1", type: "PASSPORT", status: "PROCESSED" },
    { objectId: "O2", type: "UTILITY_BILL", status: "FAILED" },
    { objectId: "O3", type: "PASSPORT", status: "PROCESSED" },
    { objectId: "O4", type: "DRIVER_LICENCE", status: "PROCESSING" },
    { objectId: "O5", type: "UTILITY_BILL", status: "PROCESSED" }
];
```

Return the IDs of **processed documents**, but only return **one document ID per document type**.

Expected:

```text
["O1", "O5"]
```

Why?

```text
PASSPORT       → O1
UTILITY_BILL   → O5
DRIVER_LICENCE → none
```

```ts
function processedDocuments(documents: ???): string[] {
    // ...
}
```

**Think:** `filter` + `Set` or `Map`.

---

# 🧠 The important part

These 10 are actually testing a fairly small number of concepts:

| Problem | Main concept              |
| ------- | ------------------------- |
| 1       | `reduce`                  |
| 2       | `filter` + `reduce`       |
| 3       | `Map`                     |
| 4       | `Map` / accumulator       |
| 5       | `Set` + `filter`          |
| 6       | `Map` counting            |
| 7       | `Map` lookup / joining    |
| 8       | `Map` counting            |
| 9       | `sort` / `reduce`         |
| 10      | `Set` / `Map` + filtering |

So don't think:

> "I have to learn 10 algorithms."

You're really learning:

**Array → Set → Map → Reduce → combinations of those.**

And that's a very manageable TypeScript interview toolkit.

### One additional thing I'd practise

For each problem, force yourself to say **before coding**:

> **"My input is X, my desired output is Y, so I need to..."**

That will make you look much stronger in the whiteboard exercise because the interviewer gets to see your reasoning rather than watching you silently type.



