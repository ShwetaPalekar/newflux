# Interview Prep Guide

Companion to the `practice-dryrun/` sandbox. Everything here is reference — skim it once, then lean on the practice exercise to make it stick.

---

## 1. React Hooks Refresher

### `useState` — component memory
```tsx
const [count, setCount] = useState(0);
setCount(count + 1);          // fine for simple cases
setCount(prev => prev + 1);   // safer when the new value depends on the old one
```
- Calling the setter schedules a re-render; it doesn't mutate `count` immediately in the current closure.
- Never mutate state directly (`arr.push(x)`); always create a new value (`setArr([...arr, x])`).

### `useEffect` — syncing with the outside world (fetches, subscriptions, timers)
```tsx
useEffect(() => {
  let cancelled = false;
  setLoading(true);

  fetch(`/api/slots?date=${dateStr}`)
    .then(res => res.json())
    .then(data => { if (!cancelled) setSlots(data.availableSlots); })
    .finally(() => { if (!cancelled) setLoading(false); });

  return () => { cancelled = true; };   // cleanup: runs before the next effect, and on unmount
}, [dateStr]);                          // dependency array: effect re-runs only when dateStr changes
```
- **Empty array `[]`** → runs once, on mount.
- **No array at all** → runs after every render (rarely what you want).
- **Missing a dependency** you actually use inside the effect is the #1 source of stale-closure bugs — the linter will flag it, trust it.
- The `cancelled` flag (or an `AbortController`) guards against setting state after the component unmounted or a newer request has already landed — matters when the user changes the date quickly.

### Controlled inputs
```tsx
const [name, setName] = useState("");
<input value={name} onChange={e => setName(e.target.value)} />
```
React owns the value; the DOM input just reflects state. This is what "controlled component" means.

### Rendering lists
```tsx
{slots.map(slot => (
  <li key={slot}>{slot}</li>
))}
```
`key` must be stable and unique per item (not array index, if the list can reorder/filter) — React uses it to match items across re-renders.

### Loading / error / empty states
The three states you almost always need when fetching:
```tsx
{loading && <p>Loading…</p>}
{error && <p role="alert">{error}</p>}
{!loading && !error && items.length === 0 && <p>Nothing here.</p>}
```

### Passing data down / events up
```tsx
// parent
<SlotList slots={slots} onBook={handleBook} />

// child
function SlotList({ slots, onBook }: { slots: string[]; onBook: (slot: string) => void }) {
  return <>{slots.map(s => <button key={s} onClick={() => onBook(s)}>{s}</button>)}</>;
}
```
Data flows down via props; the child notifies the parent via a callback prop. No magic — this is 90% of React composition.

---

## 2. React Testing Library (RTL) Refresher

Philosophy: **test what the user sees and does, not component internals.**

```tsx
import { render, screen, fireEvent, waitFor } from "@testing-library/react";

test("books a slot", async () => {
  render(<AppointmentBooker />);

  // queries — prefer accessible ones (role, label) over test-ids
  screen.getByText("09:00");            // throws if not found — use for things that should already be there
  await screen.findByText("09:00");     // async — waits for it to appear (fetch/effect not resolved yet)
  screen.queryByText("09:00");          // returns null instead of throwing — use to assert *absence*

  fireEvent.click(screen.getByRole("button", { name: "09:00" }));
  fireEvent.change(screen.getByLabelText(/name/i), { target: { value: "Ada" } });

  await waitFor(() => {
    expect(screen.getByText(/Booked/)).toBeInTheDocument();
  });
});
```

Cheat sheet:
| Need | Use |
|---|---|
| Element should already be in the DOM | `getBy...` |
| Element will appear after an async action | `findBy...` (async, awaited) |
| Assert something is *not* present | `queryBy...` (never throws) |
| Simulate a click/typing | `fireEvent.click/change` (or `userEvent` if installed) |
| Wait for a condition after an async update | `waitFor(() => expect(...))` |

**Mocking `fetch`:**
```tsx
globalThis.fetch = jest.fn(() =>
  Promise.resolve({ ok: true, json: () => Promise.resolve({ availableSlots: ["09:00"] }) })
) as unknown as typeof fetch;
```
Reset it between tests (`jest.resetAllMocks()` in `afterEach`) so one test's mock doesn't leak into the next.

Worked examples with all of this in context: `practice-dryrun/frontend/src/practice/AppointmentBooker.solution.test.tsx`.

---

## 3. Frontend ↔ Backend ↔ Frontend: the full round trip

This is the checklist for wiring up *any* new feature end-to-end — the shape of what you'll likely be asked to build live.

### Backend: receive a request, send a response
1. **Define the route** — `app.get('/api/appointments/slots', handler)` or `router.post('/book', handler)`.
2. **Read the input**:
   - Query string → `req.query.date`
   - URL param → `req.params.id` (route defined as `/book/:id`)
   - JSON body → `req.body.time` (requires `app.use(express.json())` — already set up in this scaffold)
3. **Validate the input** before doing anything else. Bad input → respond immediately with `4xx` and a clear error message; don't let it fall through to business logic.
4. **Run the business logic** — check availability, apply rules (no weekends, no double-booking), mutate your data store.
5. **Send a response**:
   - `res.status(200).json({...})` for success
   - `res.status(201).json({...})` after creating something
   - `res.status(400/404/409).json({ error: "..." })` for client errors
   - Always send *something* — a handler that never calls `res.send/json/end` hangs the request forever (the classic "forgot to respond" bug).
6. **CORS**: already handled here via `app.use(cors())` — needed because the frontend (port 5173) and backend (port 10888) are different origins.

### Frontend: call the backend, use the response, re-render
1. **Trigger the call** — usually inside `useEffect` (on mount / when a dependency changes) or an event handler (on click/submit).
2. **Make the request**:
   ```tsx
   const res = await fetch("/api/appointments/slots?date=2026-09-21");
   ```
   (This scaffold's Vite dev server proxies `/api/*` to `localhost:10888` — see `vite.config.ts` — so relative URLs work without CORS friction. Absolute `http://localhost:10888/...` also works.)
3. **Check `res.ok` before trusting the body** — `fetch` does *not* reject on 4xx/5xx, only on network failure. Always:
   ```tsx
   const body = await res.json();
   if (!res.ok) throw new Error(body.error ?? "Request failed");
   ```
4. **Update state** with the parsed response — this is what triggers the re-render.
5. **Handle the three states** in the UI: loading while the promise is pending, error if it rejected, success once data is in state.
6. **Reflect the result back to the user** — re-render the list, show a confirmation, clear a form, whatever the interaction implies.

### The full loop, one line each
`user action → event handler / effect fires → fetch() → Express route matches → validate → business logic → res.json() → fetch() promise resolves → check res.ok → setState → component re-renders → user sees the result`

If you can narrate that sentence while pointing at code, you understand the architecture — that's usually exactly what interviewers are probing for.

---

## 4. Running the dry run

In `practice-dryrun/`:

1. **Backend**: open `backend/src/practice/appointments.exercise.ts`, implement the two TODOs, then wire it into `backend/src/index.ts`:
   ```ts
   import { appointmentsRouter } from "./practice/appointments.exercise";
   app.use("/api/appointments", appointmentsRouter);
   ```
   Run it: `cd backend && npm run dev`. Test with curl/Postman/browser against `http://localhost:10888/api/appointments/slots?date=2026-09-21`.

2. **Frontend**: open `frontend/src/practice/AppointmentBooker.exercise.tsx`, implement the TODOs. Render it by temporarily swapping it into `App.tsx`:
   ```tsx
   import { AppointmentBooker } from "./practice/AppointmentBooker.exercise";
   // return <AppointmentBooker />;
   ```
   Run it: `cd frontend && npm run dev`, open `http://localhost:5173/`.

3. **Check yourself** against `appointments.solution.ts` / `AppointmentBooker.solution.tsx` once you've had a real attempt — not before.

4. **Bonus**: try writing your own tests for your exercise implementation before peeking at `*.solution.test.*` — that's the closest simulation of what "write it live" actually feels like.
