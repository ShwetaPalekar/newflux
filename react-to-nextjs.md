# Next.js in an Hour — For React Developers

Same method as last time: read a section, then immediately type it in a sandbox (`npx create-next-app@latest my-app` — choose TypeScript, App Router, Tailwind optional). Don't copy-paste.

Mental model going in: **Next.js is React, plus a file-based router, plus a server.** Everything you know about components, props, `useState`, `useEffect` still applies. What's new is *where* your code runs and *how* pages get created.

---

## 1. Routing: React Router config → folders and files

**Plain React (React Router)**
```jsx
const router = createBrowserRouter([
  { path: '/', element: <Home /> },
  { path: '/about', element: <About /> },
  { path: '/blog/:slug', element: <BlogPost /> },
]);
```

**Next.js (App Router)** — no router config file at all. The **folder structure itself is the route map**:

```
app/
  page.tsx           →  /
  about/
    page.tsx          →  /about
  blog/
    [slug]/
      page.tsx        →  /blog/:slug  (any value)
```

```tsx
// app/blog/[slug]/page.tsx
export default function BlogPost({ params }: { params: { slug: string } }) {
  return <h1>Post: {params.slug}</h1>;
}
```

Key mapping:
| React Router | Next.js |
|---|---|
| route array entry | a folder containing `page.tsx` |
| `:slug` param | `[slug]` folder name |
| `element={<Comp/>}` | the default export of `page.tsx` *is* the component |
| `<Link to="/about">` | `<Link href="/about">` (from `next/link`) |

**Try it:** Create `app/about/page.tsx` and `app/blog/[slug]/page.tsx`, visit `/about` and `/blog/hello-world` with zero router config, and log `params.slug`.

---

## 2. Layouts: wrapping `<App>` → `layout.tsx`

**Plain React** — you'd wrap everything manually:
```jsx
function App() {
  return (
    <>
      <Navbar />
      <RouterProvider router={router} />
      <Footer />
    </>
  );
}
```

**Next.js** — a `layout.tsx` in any folder wraps every `page.tsx` beneath it, and layouts nest automatically:

```tsx
// app/layout.tsx  (root layout — required, wraps the whole app)
export default function RootLayout({ children }: { children: React.ReactNode }) {
  return (
    <html lang="en">
      <body>
        <Navbar />
        {children}
        <Footer />
      </body>
    </html>
  );
}
```

Add `app/blog/layout.tsx` and it wraps *only* pages under `/blog/*`, nested inside the root layout — no extra wiring needed.

**Try it:** Add a root layout with a shared nav, then add a second layout scoped to just one route segment and confirm both nest correctly.

---

## 3. The big one: Server Components vs Client Components

This is the concept that doesn't exist in plain React at all. **By default, every component in the `app/` directory runs on the server**, not in the browser.

```tsx
// app/page.tsx — this is a Server Component by default
export default function Home() {
  console.log('This runs on the server, not in devtools console!');
  return <h1>Hello</h1>;
}
```

Server Components:
- can be `async` and fetch data directly, no `useEffect` needed
- never ship their JS to the browser (smaller bundle)
- **cannot** use `useState`, `useEffect`, `onClick`, or any browser-only API

The moment you need interactivity or hooks, opt in to a Client Component with a directive at the very top of the file:

```tsx
'use client';

import { useState } from 'react';

export default function Counter() {
  const [count, setCount] = useState(0);
  return <button onClick={() => setCount(count + 1)}>{count}</button>;
}
```

| Plain React | Next.js |
|---|---|
| every component runs in the browser | components run on the server unless marked `'use client'` |
| `useState`/`useEffect` always available | only available in `'use client'` files |
| you fetch data with `useEffect` + `fetch` | Server Components fetch directly with `await`, no hook needed |

**Try it:** Build a page with a Server Component parent (no directive) that renders a Client Component child (`'use client'`, holding a counter). Confirm in devtools that only the counter's JS shows up in the Network tab, not the parent's.

---

## 4. Data fetching: `useEffect` + `fetch` → `async` Server Components

**Plain React**
```jsx
function Users() {
  const [users, setUsers] = useState([]);
  useEffect(() => {
    fetch('/api/users').then(r => r.json()).then(setUsers);
  }, []);
  return <ul>{users.map(u => <li key={u.id}>{u.name}</li>)}</ul>;
}
```

**Next.js — just `await` it directly in the component:**
```tsx
export default async function Users() {
  const res = await fetch('https://jsonplaceholder.typicode.com/users');
  const users = await res.json();
  return <ul>{users.map((u: any) => <li key={u.id}>{u.name}</li>)}</ul>;
}
```

No `useState`, no `useEffect`, no loading flicker to manage manually — the server waits for the data before sending HTML to the browser. (Next.js also caches this `fetch` automatically by default.)

**Try it:** Rewrite your step-7-from-last-time user list (the one using `useEffect`) as an `async` Server Component. Notice how much code disappears.

---

## 5. Loading and error states: manual flags → special files

**Plain React** — you write the conditional yourself:
```jsx
{loading ? <Spinner /> : <UserList users={users} />}
```

**Next.js** — drop a `loading.tsx` next to any `page.tsx` and Next shows it automatically while the page's data is being fetched:

```
app/
  users/
    page.tsx      ← your async Server Component
    loading.tsx   ← shown instantly while page.tsx awaits data
    error.tsx     ← shown if page.tsx throws
```

```tsx
// app/users/loading.tsx
export default function Loading() {
  return <p>Loading users...</p>;
}
```

**Try it:** Add a `loading.tsx` next to your Users page from step 4, artificially slow the fetch down (`await new Promise(r => setTimeout(r, 1000))`), and watch it appear automatically.

---

## 6. API routes: your own backend, in the same project

Plain React has no backend at all — you always call *someone else's* API. Next.js lets you write one:

```ts
// app/api/hello/route.ts
export async function GET() {
  return Response.json({ message: 'Hello from the server' });
}
```

Visiting `/api/hello` in the browser (or fetching it from a Client Component) hits this handler. This is the closest thing to writing your own tiny backend without leaving your React project.

**Try it:** Create `app/api/users/route.ts` that returns a hardcoded JSON array, then fetch it from a Client Component with `useEffect` (this part *does* need a Client Component, since the fetch is happening from the browser after a click, not at initial render).

---

## 7. Images and fonts: `<img>` → optimized built-ins

**Plain React**
```jsx
<img src="/photo.jpg" alt="A photo" />
```

**Next.js** — automatic resizing, lazy loading, and layout-shift prevention:
```tsx
import Image from 'next/image';

<Image src="/photo.jpg" alt="A photo" width={400} height={300} />
```

Similarly, fonts are imported and optimized rather than linked via a `<link>` tag:
```tsx
import { Inter } from 'next/font/google';
const inter = Inter({ subsets: ['latin'] });
```

**Try it:** Swap an `<img>` for `<Image>` in your layout and note the required `width`/`height` props (Next uses these to reserve space and avoid layout shift).

---

## Quick-reference cheat sheet

| Plain React | Next.js (App Router) |
|---|---|
| React Router config file | folder = route, `page.tsx` = the page |
| `:param` in a route string | `[param]` folder name |
| manual `<Navbar>`/`<Footer>` wrapping | `layout.tsx`, nests automatically |
| every component runs in-browser | Server Component by default; `'use client'` opts in to browser |
| `useState`/`useEffect` everywhere | only inside `'use client'` files |
| `useEffect` + `fetch` for data | `await fetch(...)` directly in an `async` component |
| manual `loading ? <Spinner/> : ...` | `loading.tsx` file, automatic |
| manual try/catch + error UI | `error.tsx` file, automatic |
| no backend — always calling someone else's API | `app/api/.../route.ts` — write your own |
| `<img>` | `<Image>` from `next/image` (auto-optimized) |

## What changes about your React knowledge (and what doesn't)
- Components, props, JSX syntax: **identical**, no relearning.
- `useState`, `useEffect`, custom hooks: **identical, but only inside `'use client'` files.**
- The one real mind-shift: stop asking "how do I fetch this in `useEffect`?" and start asking "does this even need to run in the browser?" Most data-fetching, non-interactive parts of a page don't — leave them as Server Components and `await` directly.

## Suggested 60-minute pacing
- 0–10 min: section 1 (file-based routing — this replaces everything you knew about router config)
- 10–15 min: section 2 (layouts)
- 15–30 min: section 3 (Server vs Client Components — the core "click," don't rush this one)
- 30–40 min: section 4 (data fetching without useEffect)
- 40–45 min: section 5 (loading/error files)
- 45–55 min: section 6 (API routes)
- 55–60 min: section 7 + cheat sheet
