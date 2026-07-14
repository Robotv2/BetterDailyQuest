function addDocumentationFeedback() {
  const content = document.querySelector(".md-content__inner");
  if (!content || content.querySelector(".bdq-feedback")) {
    return;
  }

  const title = document.querySelector("h1")?.childNodes[0]?.textContent?.trim() || "administrator guide";
  const pageUrl = window.location.href.split("#")[0];
  const issueTitle = `[Docs] ${title}`;
  const issueBody = `Page: ${pageUrl}\n\nWhat is unclear or incorrect?\n`;

  const feedback = document.createElement("nav");
  feedback.className = "bdq-feedback";
  feedback.setAttribute("aria-label", "Documentation feedback");

  const issue = document.createElement("a");
  issue.href = `https://github.com/Robotv2/BetterDailyQuest/issues/new?title=${encodeURIComponent(issueTitle)}&body=${encodeURIComponent(issueBody)}`;
  issue.textContent = "Report a documentation problem";

  const edit = document.createElement("a");
  const isIndexPage = window.location.pathname.endsWith("/");
  const relativePath = window.location.pathname
    .replace(/^\/BetterDailyQuest\/?/, "")
    .replace(/^\//, "")
    .replace(/\/$/, "");
  const sourcePath = !relativePath ? "index.md" : isIndexPage ? `${relativePath}/index.md` : `${relativePath}.md`;
  edit.href = `https://github.com/Robotv2/BetterDailyQuest/edit/master/docs/${sourcePath}`;
  edit.textContent = "Edit this page";

  feedback.append(issue, edit);
  content.append(feedback);
}

if (typeof document$ !== "undefined") {
  document$.subscribe(addDocumentationFeedback);
} else {
  document.addEventListener("DOMContentLoaded", addDocumentationFeedback);
}
