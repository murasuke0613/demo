import React from "react";
import { createRoot } from "react-dom/client";
import PostList from "./PostList";

const root = createRoot(document.getElementById("root")!);
root.render(<PostList />);
