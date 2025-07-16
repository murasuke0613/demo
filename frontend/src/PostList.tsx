import axios from "axios";
import "bootstrap-icons/font/bootstrap-icons.css";
import "bootstrap/dist/css/bootstrap.min.css";
import React, { useEffect, useState } from "react";
import "./PostList.css";

// 🌱 APIの戻り値用型定義
type Post = {
  writingId: number;
  userNameStamp: string;
  jobNameStamp: string;
  departmentNameStamp: string;
  message: string;
  writingTime: string;
  pdfMovie?: string | null;
};

type UserResponse = {
  userName: string;
  userId: string;
};

const PostList: React.FC = () => {
  const [posts, setPosts] = useState<Post[]>([]);
  const [sort, setSort] = useState<"latest" | "oldest">("latest");
  const [userName, setUserName] = useState<string>("ゲスト");
  const [userId, setUserId] = useState<string>("N/A");

  // 🌱 ユーザー情報取得
  useEffect(() => {
    axios
      .get<UserResponse>("/api/user")
      .then((res) => {
        setUserName(res.data.userName || "ゲスト");
        setUserId(res.data.userId || "N/A");
      })
      .catch((err) => console.error("ユーザー情報取得失敗:", err));
  }, []);

  // 🌱 投稿データ取得
  useEffect(() => {
    axios
      .get<Post[]>("/api/posts")
      .then((res) => setPosts(res.data))
      .catch((err) => console.error("投稿データ取得失敗:", err));
  }, []);

  const sortedPosts = [...posts]
    .sort((a, b) =>
      sort === "latest"
        ? new Date(b.writingTime).getTime() - new Date(a.writingTime).getTime()
        : new Date(a.writingTime).getTime() - new Date(b.writingTime).getTime()
    )
    .slice(0, 20); // ✅ 最大20件に制限

  const daysSince = (dateStr: string) => {
    const date = new Date(dateStr);
    const now = new Date();
    return Math.floor((now.getTime() - date.getTime()) / (1000 * 60 * 60 * 24));
  };

  return (
    <div>
      {/* ===== ナビバー ===== */}
      <nav className="navbar navbar-expand-lg navbar-dark shadow">
        <div className="container-fluid">
          <a className="navbar-brand fw-bold" href="/latestPosts">
            <i className="bi bi-house-door-fill"></i> Home
          </a>
          <div className="ms-auto user-info d-flex align-items-center">
            <span>
              <i className="bi bi-person-circle"></i>{" "}
              <strong>{userName}</strong>
            </span>
            <span className="ms-3">
              ID: <span>{userId}</span>
            </span>

            {/* 🔄 最新情報ボタン */}
            <a
              href="/latestPosts"
              className="btn btn-light btn-sm ms-3"
            >
              <i className="bi bi-clock-history"></i> 最新情報
            </a>

            <a
              href="/logout"
              className="btn btn-sm btn-outline-light ms-3"
            >
              <i className="bi bi-box-arrow-right"></i> ログアウト
            </a>
          </div>
        </div>
      </nav>

      {/* ===== メインコンテンツ ===== */}
      <div className="container py-4" id="latestPostsContainer">
        <h2 className="mb-4 text-center">📋 各部署の投稿</h2>

        <div className="text-center mb-3">
          <button
            className={`btn btn-sm ${
              sort === "latest" ? "btn-primary" : "btn-outline-primary"
            } me-2`}
            onClick={() => setSort("latest")}
          >
            最新順
          </button>
          <button
            className={`btn btn-sm ${
              sort === "oldest" ? "btn-primary" : "btn-outline-primary"
            }`}
            onClick={() => setSort("oldest")}
          >
            古い順
          </button>
        </div>

        <div className="row row-cols-1 row-cols-md-2 g-4">
          {sortedPosts.map((post) => (
            <div className="col" key={post.writingId}>
              <div className="card h-100 shadow-sm border-0 rounded-4">
                <div className="card-header bg-primary text-white d-flex justify-content-between">
                  <span>
                    <i className="bi bi-building"></i>{" "}
                    {post.departmentNameStamp}
                  </span>
                  {daysSince(post.writingTime) < 7 && (
                    <span className="badge bg-warning text-dark ms-2">
                      {daysSince(post.writingTime) === 0
                        ? "New"
                        : daysSince(post.writingTime) === 1
                        ? "1日前"
                        : daysSince(post.writingTime) === 2
                        ? "2日前"
                        : "今週"}
                    </span>
                  )}
                </div>
                <div className="card-body">
                  <h5 className="card-title mb-2">
                    <i className="bi bi-person-circle"></i>{" "}
                    {post.userNameStamp}
                    <span className="ms-2">
                      <i className="bi bi-briefcase-fill"></i>{" "}
                      {post.jobNameStamp}
                    </span>
                  </h5>
                  <div
                    className="card-text"
                    dangerouslySetInnerHTML={{ __html: post.message }}
                  ></div>
                  {post.pdfMovie && (
                    <a
                      href={`/api/download/${post.writingId}`}
                      target="_blank"
                      rel="noopener noreferrer"
                      className="btn btn-outline-secondary btn-sm mt-2"
                    >
                      <i className="bi bi-file-earmark-pdf"></i> 添付PDFを見る
                    </a>
                  )}
                  <div className="text-end text-muted small">
                    <i className="bi bi-clock"></i>{" "}
                    {new Date(post.writingTime).toLocaleString()}
                  </div>
                </div>
              </div>
            </div>
          ))}

          {sortedPosts.length === 0 && (
            <div className="col">
              <div className="text-center text-muted">
                <i className="bi bi-info-circle"></i> 投稿はありません
              </div>
            </div>
          )}
        </div>
      </div>
    </div>
  );
};

export default PostList;
