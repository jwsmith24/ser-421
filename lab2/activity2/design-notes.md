# Browser Persistence

Local storage vs session storage:
- Local storage:
  - stores data persistently in the browser and will remain until explicitly cleared.
  - Survives closing the tab, window/browser, and even restarting the computer
  - Data is shared across all tabs and windows from the same origin URL
  - Best for: user preferences, shopping cart contents, cached API responses

- Session storage:
  - stores data for the life of the browser tab
  - data is automatically cleared when tab is closed
  - each tab/window has its own session storage
  - best for: state during a single visit or action on the page

- Other considerations:
  - Both are synchronous/blocking operations
  - Cannot be accessed from another origin

**_Due to the requirement stating conversations with Eliza must persist across browser restarts, 
local storage is the best fit._**