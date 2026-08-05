# React in an Hour — For Angular Developers

Read a section, then immediately type the code yourself in a sandbox (e.g. [stackblitz.com/fork/react](https://stackblitz.com) or `npx create-react-app my-app --template typescript`). Don't copy-paste — retyping is what makes it stick.

Mental model going in: **you already know this.** React and Angular solve the same problems. Below, every concept is shown as an Angular → React pair.

---

## 1. Components: class/template → function/JSX

**Angular**
```ts
@Component({
  selector: 'app-jumbotron',
  standalone: true,
  template: `<div class="jumbotron"><h1>{{ title }}</h1></div>`
})
export class JumbotronComponent {
  title = 'Hello';
}
```

**React**
```jsx
function Jumbotron() {
  return (
    <div className="jumbotron">
      <h1>Hello</h1>
    </div>
  );
}
```

Key mapping:
| Angular | React |
|---|---|
| `selector` | the function's **name** |
| template string | JSX (HTML-looking syntax embedded in JS) |
| `class="..."` | `className="..."` (because `class` is a reserved JS word) |
| `{{ expr }}` | `{ expr }` (one curly brace, not two) |

**Try it:** Create `Jumbotron.jsx`, write the function above, then render it inside your `App` component: `<Jumbotron />` (PascalCase — this is how React tells a custom component apart from a plain HTML tag like `<div>`).

---

## 2. Inputs → Props

**Angular**
```ts
@Component({ selector: 'app-jumbotron', template: `<h1>{{ title }}</h1><p>{{ description }}</p>` })
export class JumbotronComponent {
  @Input() title!: string;
  @Input() description!: string;
}
```
```html
<app-jumbotron title="Some text" description="More text"></app-jumbotron>
```

**React**
```jsx
function Jumbotron(props) {
  return (
    <div>
      <h1>{props.title}</h1>
      <p>{props.description}</p>
    </div>
  );
}
```
```jsx
<Jumbotron title="Some text" description="More text" />
```

Props are just a plain object passed as the function's single argument — nothing magic. A common style is to destructure it: `function Jumbotron({ title, description }) { ... }`.

**Try it:** Add a `description` prop to your Jumbotron and pass a different value from two places it's used.

---

## 3. Typing props (TypeScript)

**React + TS**
```tsx
interface JumbotronProps {
  title: string;
  description: string;
}

function Jumbotron({ title, description }: JumbotronProps) {
  return (
    <div>
      <h1>{title}</h1>
      <p>{description}</p>
    </div>
  );
}
```

Note the file extension: `.tsx`, not `.ts` — you need both the TypeScript compiler *and* the JSX compiler, hence the combined extension.

**Try it:** Rename your file to `.tsx`, add the interface, and delete a required prop from a usage to watch the compiler complain (that's the point — same safety net you get from Angular's `@Input()` types).

---

## 4. Control flow: `@if`/`@for` → plain JavaScript

Angular gives you built-in template syntax (`@if`, `@for`, or the older `*ngIf`/`*ngFor`). **React has no template DSL at all — it's just JavaScript inside `{ }`.**

**Conditional:**
```jsx
{title && <h1>{title}</h1>}
```
(if `title` is truthy, render the `<h1>`; otherwise render nothing — logical AND short-circuits)

**Looping — Angular's `@for` vs React's `.map()`:**
```jsx
const descriptions = ['First', 'Second', 'Third'];

<div>
  {descriptions.map((desc, index) => (
    <p key={index}>{desc}</p>
  ))}
</div>
```

`key` is the React equivalent of Angular's `track` — and unlike `*ngFor`, React makes it **mandatory** (you'll get a console error without it).

**Try it:** Render a list of 3–4 items from an array using `.map()`, and try a conditional heading that only shows when a variable is truthy.

---

## 5. Component state: signals/properties → `useState`

**Angular**
```ts
export class Counter {
  count = signal(0);
  increment() { this.count.update(c => c + 1); }
}
```

**React**
```jsx
import { useState } from 'react';

function Counter() {
  const [count, setCount] = useState(0);

  return (
    <div>
      <p>{count}</p>
      <button onClick={() => setCount(count + 1)}>Increment</button>
    </div>
  );
}
```

`useState(initialValue)` returns a **2-item array**: `[currentValue, setterFunction]`. You only get a re-render when you call the setter — changing a plain variable does nothing visible (React never even finds out).

This is the single most important rule to internalize: **React re-renders only when props change, or when a state setter is called.** Nothing else.

**Try it:** Build the exact example from the video: two variables holding the same string, one via `useState`, one a plain `let`. Add a button that updates both. Only the `useState` one will visibly change — prove this to yourself, it's the "aha" moment for React's mental model.

---

## 6. Virtual DOM (why React re-renders "everything" cheaply)

React re-renders the *entire* component's JSX in memory (the "virtual DOM") every time state changes, then diffs it against what's currently on screen, and patches only the real DOM nodes that actually changed.

You don't write any code for this — it's just useful to know so features like a ticking clock make sense: you can re-render a whole `<div>` every second, and only the second-hand text updates in the real DOM, not the surrounding markup.

No exercise needed here — just a mental note for later when you're debugging performance.

---

## 7. `ngOnInit`/constructor → `useEffect`

**Angular**
```ts
export class UserList implements OnInit {
  users: User[] = [];
  constructor(private http: HttpClient) {}
  ngOnInit() {
    this.http.get<User[]>('/api/users').subscribe(data => this.users = data);
  }
}
```

**React**
```jsx
import { useState, useEffect } from 'react';

function UserList() {
  const [users, setUsers] = useState([]);

  useEffect(() => {
    fetch('/api/users')
      .then(res => res.json())
      .then(data => setUsers(data));
  }, []); // empty array = run once, like ngOnInit

  return (
    <ul>
      {users.map(u => <li key={u.id}>{u.name}</li>)}
    </ul>
  );
}
```

No `HttpClient` — React just uses the browser's native `fetch`, which returns a Promise (not an Observable), hence the two `.then()`s: one to parse JSON, one to use the data.

The `[]` second argument to `useEffect` is the dependency array — empty means "run once on mount," which is your `ngOnInit` equivalent.

**Try it:** Fetch from a public test API (e.g. `https://jsonplaceholder.typicode.com/users`) inside `useEffect`, store the result with `useState`, render it in a list.

---

## 8. Services/DI → custom hooks

**Angular** — inject a service:
```ts
constructor(private cart: CartService) {}
```

**React** — write your own hook (a function prefixed `use...` that itself can call `useState`/`useEffect`):
```jsx
function useCart() {
  const [items, setItems] = useState([]);
  const addItem = (item) => setItems([...items, item]);
  return { items, addItem };
}

function CartPage() {
  const { items, addItem } = useCart();
  // ...
}
```

Key difference from Angular services: **a custom hook is not a singleton.** Every component that calls `useCart()` gets its own independent state — it doesn't share data across components the way an injected Angular service instance does.

**Try it:** Extract your Counter's state logic from step 5 into a `useCounter()` hook, then use it in two different components and confirm they don't share the count.

---

## 9. Router: `RouterModule` → React Router (third-party)

```jsx
import { createBrowserRouter, RouterProvider } from 'react-router-dom';

const router = createBrowserRouter([
  { path: '/', element: <Home /> },
  { path: '/about', element: <About /> },
]);

function App() {
  return <RouterProvider router={router} />;
}
```

Conceptually identical to Angular's route config + `<router-outlet>` — just a separate npm package (`react-router-dom`) rather than built into the framework, and it evolves independently of React itself (expect breaking changes between major versions — worth reading the migration guide when you upgrade).

**Try it:** only if you have time left — set up two routes and a `<Link to="/about">` (React Router's equivalent of `routerLink`).

---

## Quick-reference cheat sheet

| Angular | React |
|---|---|
| `@Component` class | function (or, legacy, a class extending `React.Component`) |
| `selector` | function name, used as `<ComponentName />` |
| template string | JSX |
| `@Input()` | function parameter (`props`) |
| `{{ expr }}` | `{ expr }` |
| `class="x"` | `className="x"` |
| `*ngIf` / `@if` | `{cond && <Elem/>}` or ternary |
| `*ngFor` / `@for` + `track` | `.map()` + mandatory `key` |
| signal / class property | `useState` |
| `ngOnInit` | `useEffect(fn, [])` |
| `HttpClient` | `fetch` (native, returns Promises) |
| injected service | custom hook (not a singleton!) |
| Angular Router | React Router (separate library) |
| `.ts` | `.tsx` |

## What's genuinely missing (no built-in equivalent)
Forms, HTTP client, routing, DI, two-way binding — all need third-party libraries in React (most commonly: React Hook Form, plain `fetch`/axios, React Router). Angular bundles all of this; React deliberately doesn't, trading batteries-included consistency for flexibility and a smaller footprint.

## Suggested 60-minute pacing
- 0–10 min: sections 1–2 (components, props)
- 10–20 min: section 3–4 (TS + control flow)
- 20–35 min: section 5 (state — do the two-variable exercise, it's the core "click")
- 35–45 min: section 7 (fetch + useEffect)
- 45–55 min: section 8 (custom hook)
- 55–60 min: skim section 9 + cheat sheet
