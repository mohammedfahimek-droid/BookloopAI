package com.bookloop.web;

/**
 * Modern, responsive Single-Page Application (SPA) HTML for BookLoop AI.
 * Built with vanilla HTML5, CSS3, and modern ES6 JavaScript. Zero build step required.
 */
public class BookLoopHtml {

    public static String getHtml() {
        return """
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>BookLoop AI - Campus Circular Economy & Barter Platform</title>
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Plus+Jakarta+Sans:wght@300;400;500;600;700;800&family=JetBrains+Mono:wght@400;600&display=swap" rel="stylesheet">
    <style>
        :root {
            --bg-base: #0a0d14;
            --bg-card: #111726;
            --bg-card-hover: #162035;
            --bg-input: #1a233a;
            --border: #23304c;
            --border-hover: #3b82f6;
            --primary: #10b981;
            --primary-glow: rgba(16, 185, 129, 0.25);
            --accent: #3b82f6;
            --accent-purple: #8b5cf6;
            --text-main: #f3f4f6;
            --text-muted: #9ca3af;
            --text-dim: #6b7280;
            --badge-exchange: #8b5cf6;
            --badge-sell: #3b82f6;
            --badge-donate: #10b981;
            --badge-request: #f59e0b;
        }

        * {
            box-sizing: border-box;
            margin: 0;
            padding: 0;
        }

        body {
            font-family: 'Plus Jakarta Sans', sans-serif;
            background-color: var(--bg-base);
            color: var(--text-main);
            min-height: 100vh;
            display: flex;
            flex-direction: column;
            overflow-x: hidden;
            line-height: 1.5;
        }

        /* Ambient glowing background gradients */
        .ambient-glow {
            position: fixed;
            top: 0;
            left: 50%;
            transform: translateX(-50%);
            width: 1000px;
            height: 400px;
            background: radial-gradient(ellipse at center, rgba(16, 185, 129, 0.12) 0%, rgba(59, 130, 246, 0.08) 45%, transparent 70%);
            pointer-events: none;
            z-index: 0;
        }

        /* Header Navbar */
        header {
            position: sticky;
            top: 0;
            z-index: 50;
            backdrop-filter: blur(16px);
            background: rgba(10, 13, 20, 0.82);
            border-bottom: 1px solid var(--border);
            padding: 0.85rem 2rem;
            display: flex;
            align-items: center;
            justify-content: space-between;
        }

        .brand {
            display: flex;
            align-items: center;
            gap: 0.75rem;
            text-decoration: none;
            color: white;
        }

        .brand-icon {
            width: 38px;
            height: 38px;
            background: linear-gradient(135deg, #10b981, #3b82f6);
            border-radius: 10px;
            display: flex;
            align-items: center;
            justify-content: center;
            font-size: 1.3rem;
            box-shadow: 0 0 20px rgba(16, 185, 129, 0.4);
        }

        .brand-text h1 {
            font-size: 1.25rem;
            font-weight: 800;
            letter-spacing: -0.02em;
            background: linear-gradient(to right, #ffffff, #93c5fd);
            -webkit-background-clip: text;
            -webkit-text-fill-color: transparent;
        }

        .brand-text span {
            font-size: 0.7rem;
            text-transform: uppercase;
            letter-spacing: 0.08em;
            color: var(--primary);
            font-weight: 700;
        }

        .nav-right {
            display: flex;
            align-items: center;
            gap: 1rem;
        }

        .live-pill {
            display: flex;
            align-items: center;
            gap: 0.4rem;
            background: rgba(16, 185, 129, 0.1);
            border: 1px solid rgba(16, 185, 129, 0.3);
            color: #34d399;
            font-size: 0.75rem;
            font-weight: 600;
            padding: 0.3rem 0.75rem;
            border-radius: 9999px;
        }

        .pulse-dot {
            width: 7px;
            height: 7px;
            border-radius: 50%;
            background-color: #10b981;
            box-shadow: 0 0 8px #10b981;
            animation: pulse 2s infinite;
        }

        @keyframes pulse {
            0%, 100% { opacity: 1; transform: scale(1); }
            50% { opacity: 0.4; transform: scale(0.85); }
        }

        /* User Switcher */
        .user-switcher-container {
            display: flex;
            align-items: center;
            gap: 0.5rem;
            background: var(--bg-card);
            border: 1px solid var(--border);
            padding: 0.35rem 0.75rem;
            border-radius: 12px;
        }

        .user-avatar {
            width: 30px;
            height: 30px;
            border-radius: 50%;
            background: linear-gradient(135deg, #6366f1, #a855f7);
            color: white;
            font-weight: 700;
            font-size: 0.75rem;
            display: flex;
            align-items: center;
            justify-content: center;
        }

        .user-meta {
            display: flex;
            flex-direction: column;
            line-height: 1.1;
        }

        .user-meta-name {
            font-size: 0.8rem;
            font-weight: 700;
        }

        .user-meta-karma {
            font-size: 0.68rem;
            color: #34d399;
            font-weight: 600;
        }

        select.user-select {
            background: var(--bg-input);
            color: var(--text-main);
            border: 1px solid var(--border);
            padding: 0.35rem 0.6rem;
            border-radius: 8px;
            font-size: 0.75rem;
            cursor: pointer;
            outline: none;
        }

        .btn {
            display: inline-flex;
            align-items: center;
            gap: 0.45rem;
            padding: 0.55rem 1.1rem;
            border-radius: 10px;
            font-weight: 600;
            font-size: 0.85rem;
            cursor: pointer;
            transition: all 0.2s ease;
            border: none;
            outline: none;
        }

        .btn-primary {
            background: linear-gradient(135deg, #10b981, #059669);
            color: white;
            box-shadow: 0 4px 14px rgba(16, 185, 129, 0.3);
        }

        .btn-primary:hover {
            box-shadow: 0 6px 20px rgba(16, 185, 129, 0.45);
            transform: translateY(-1px);
        }

        .btn-secondary {
            background: var(--bg-card);
            border: 1px solid var(--border);
            color: var(--text-main);
        }

        .btn-secondary:hover {
            background: var(--bg-card-hover);
            border-color: var(--border-hover);
        }

        .btn-sm {
            padding: 0.35rem 0.75rem;
            font-size: 0.75rem;
            border-radius: 8px;
        }

        /* Main Container */
        main {
            position: relative;
            z-index: 1;
            max-width: 1320px;
            width: 100%;
            margin: 0 auto;
            padding: 1.5rem 1.5rem 4rem 1.5rem;
            flex: 1;
        }

        /* Stats Impact Ribbon */
        .impact-ribbon {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(220px, 1fr));
            gap: 1rem;
            margin-bottom: 2rem;
        }

        .stat-card {
            background: var(--bg-card);
            border: 1px solid var(--border);
            border-radius: 14px;
            padding: 1.1rem 1.3rem;
            display: flex;
            align-items: center;
            gap: 1rem;
            transition: transform 0.2s ease;
        }

        .stat-card:hover {
            transform: translateY(-2px);
            border-color: rgba(59, 130, 246, 0.4);
        }

        .stat-icon {
            width: 44px;
            height: 44px;
            border-radius: 12px;
            display: flex;
            align-items: center;
            justify-content: center;
            font-size: 1.4rem;
        }

        .stat-icon.green { background: rgba(16, 185, 129, 0.15); color: #10b981; }
        .stat-icon.blue { background: rgba(59, 130, 246, 0.15); color: #3b82f6; }
        .stat-icon.purple { background: rgba(139, 92, 246, 0.15); color: #8b5cf6; }
        .stat-icon.amber { background: rgba(245, 158, 11, 0.15); color: #f59e0b; }

        .stat-info .stat-num {
            font-size: 1.45rem;
            font-weight: 800;
            color: #ffffff;
            letter-spacing: -0.02em;
        }

        .stat-info .stat-label {
            font-size: 0.75rem;
            color: var(--text-muted);
            text-transform: uppercase;
            font-weight: 600;
            letter-spacing: 0.04em;
        }

        /* Tabs Navigation */
        .tabs-header {
            display: flex;
            align-items: center;
            gap: 0.5rem;
            border-bottom: 1px solid var(--border);
            margin-bottom: 1.5rem;
            overflow-x: auto;
            padding-bottom: 0.2rem;
        }

        .tab-btn {
            background: transparent;
            border: none;
            color: var(--text-muted);
            font-weight: 600;
            font-size: 0.9rem;
            padding: 0.65rem 1.2rem;
            border-radius: 10px 10px 0 0;
            cursor: pointer;
            transition: all 0.2s ease;
            display: flex;
            align-items: center;
            gap: 0.45rem;
            position: relative;
        }

        .tab-btn:hover {
            color: var(--text-main);
            background: rgba(255, 255, 255, 0.03);
        }

        .tab-btn.active {
            color: #ffffff;
            background: var(--bg-card);
            border: 1px solid var(--border);
            border-bottom: 1px solid var(--bg-card);
            margin-bottom: -1px;
        }

        .tab-btn.active::after {
            content: '';
            position: absolute;
            top: -1px;
            left: 0;
            right: 0;
            height: 2px;
            background: linear-gradient(90deg, #10b981, #3b82f6);
            border-radius: 2px 2px 0 0;
        }

        .tab-badge {
            background: rgba(139, 92, 246, 0.2);
            color: #a78bfa;
            font-size: 0.7rem;
            font-weight: 700;
            padding: 0.15rem 0.5rem;
            border-radius: 9999px;
        }

        /* Tab Content Panes */
        .tab-pane {
            display: none;
        }

        .tab-pane.active {
            display: block;
            animation: fadeIn 0.2s ease-in-out;
        }

        @keyframes fadeIn {
            from { opacity: 0; transform: translateY(4px); }
            to { opacity: 1; transform: translateY(0); }
        }

        /* Tab 1: AI Smart Search */
        .ai-search-hero {
            background: linear-gradient(135deg, rgba(17, 23, 38, 0.9), rgba(22, 32, 53, 0.7));
            border: 1px solid var(--border);
            border-radius: 16px;
            padding: 2rem;
            margin-bottom: 2rem;
            position: relative;
            overflow: hidden;
        }

        .ai-search-hero h2 {
            font-size: 1.5rem;
            font-weight: 800;
            margin-bottom: 0.5rem;
            display: flex;
            align-items: center;
            gap: 0.6rem;
        }

        .ai-search-hero p {
            color: var(--text-muted);
            font-size: 0.9rem;
            max-width: 680px;
            margin-bottom: 1.5rem;
        }

        .search-input-wrapper {
            display: flex;
            gap: 0.75rem;
            margin-bottom: 1rem;
        }

        .search-input {
            flex: 1;
            background: var(--bg-input);
            border: 1px solid var(--border);
            color: white;
            font-family: inherit;
            font-size: 1rem;
            padding: 0.85rem 1.25rem;
            border-radius: 12px;
            outline: none;
            transition: border-color 0.2s;
        }

        .search-input:focus {
            border-color: #3b82f6;
            box-shadow: 0 0 0 3px rgba(59, 130, 246, 0.2);
        }

        .quick-chips {
            display: flex;
            flex-wrap: wrap;
            align-items: center;
            gap: 0.5rem;
        }

        .quick-chips span {
            font-size: 0.75rem;
            color: var(--text-dim);
            font-weight: 600;
        }

        .chip {
            background: rgba(255, 255, 255, 0.05);
            border: 1px solid rgba(255, 255, 255, 0.1);
            color: #cbd5e1;
            font-size: 0.75rem;
            padding: 0.3rem 0.7rem;
            border-radius: 9999px;
            cursor: pointer;
            transition: all 0.15s ease;
        }

        .chip:hover {
            background: rgba(59, 130, 246, 0.15);
            border-color: rgba(59, 130, 246, 0.4);
            color: #93c5fd;
        }

        /* AI Intent Result Card */
        .intent-card {
            display: none;
            background: rgba(16, 185, 129, 0.05);
            border: 1px solid rgba(16, 185, 129, 0.3);
            border-radius: 12px;
            padding: 1rem 1.25rem;
            margin-bottom: 1.5rem;
        }

        .intent-title {
            font-size: 0.8rem;
            text-transform: uppercase;
            font-weight: 700;
            color: #34d399;
            letter-spacing: 0.05em;
            margin-bottom: 0.5rem;
            display: flex;
            align-items: center;
            gap: 0.4rem;
        }

        .intent-grid {
            display: flex;
            flex-wrap: wrap;
            gap: 1.25rem;
            font-size: 0.82rem;
        }

        .intent-item b {
            color: white;
            font-weight: 600;
        }

        .intent-item span {
            color: var(--text-muted);
        }

        /* Books Grid */
        .books-grid {
            display: grid;
            grid-template-columns: repeat(auto-fill, minmax(290px, 1fr));
            gap: 1.25rem;
        }

        .book-card {
            background: var(--bg-card);
            border: 1px solid var(--border);
            border-radius: 14px;
            padding: 1.25rem;
            display: flex;
            flex-direction: column;
            justify-content: space-between;
            transition: all 0.2s ease;
            position: relative;
        }

        .book-card:hover {
            border-color: rgba(59, 130, 246, 0.4);
            transform: translateY(-2px);
            box-shadow: 0 10px 25px rgba(0, 0, 0, 0.3);
        }

        .book-card-top {
            display: flex;
            justify-content: space-between;
            align-items: flex-start;
            margin-bottom: 0.75rem;
        }

        .badge-mode {
            font-size: 0.7rem;
            font-weight: 700;
            text-transform: uppercase;
            padding: 0.25rem 0.6rem;
            border-radius: 6px;
            letter-spacing: 0.03em;
        }

        .mode-EXCHANGE { background: rgba(139, 92, 246, 0.15); color: #c4b5fd; border: 1px solid rgba(139, 92, 246, 0.3); }
        .mode-SELL { background: rgba(59, 130, 246, 0.15); color: #93c5fd; border: 1px solid rgba(59, 130, 246, 0.3); }
        .mode-DONATE { background: rgba(16, 185, 129, 0.15); color: #6ee7b7; border: 1px solid rgba(16, 185, 129, 0.3); }
        .mode-REQUEST { background: rgba(245, 158, 11, 0.15); color: #fcd34d; border: 1px solid rgba(245, 158, 11, 0.3); }

        .score-pill {
            background: linear-gradient(135deg, #059669, #10b981);
            color: white;
            font-weight: 800;
            font-size: 0.72rem;
            padding: 0.2rem 0.55rem;
            border-radius: 9999px;
            box-shadow: 0 0 10px rgba(16, 185, 129, 0.4);
        }

        .book-title {
            font-size: 1.05rem;
            font-weight: 700;
            color: #ffffff;
            margin-bottom: 0.3rem;
            line-height: 1.3;
        }

        .book-author {
            font-size: 0.8rem;
            color: var(--text-muted);
            margin-bottom: 0.5rem;
        }

        .book-meta-chips {
            display: flex;
            flex-wrap: wrap;
            gap: 0.4rem;
            margin-bottom: 0.75rem;
        }

        .meta-chip {
            font-size: 0.7rem;
            background: var(--bg-input);
            color: #cbd5e1;
            padding: 0.2rem 0.5rem;
            border-radius: 4px;
        }

        .match-explanation {
            font-size: 0.72rem;
            background: rgba(16, 185, 129, 0.08);
            border-left: 2px solid #10b981;
            color: #a7f3d0;
            padding: 0.35rem 0.6rem;
            border-radius: 0 6px 6px 0;
            margin-bottom: 0.75rem;
        }

        .exchange-wants {
            font-size: 0.75rem;
            background: rgba(139, 92, 246, 0.08);
            border-left: 2px solid #8b5cf6;
            color: #ddd6fe;
            padding: 0.35rem 0.6rem;
            border-radius: 0 6px 6px 0;
            margin-bottom: 0.75rem;
        }

        .book-footer {
            border-top: 1px solid var(--border);
            padding-top: 0.75rem;
            display: flex;
            align-items: center;
            justify-content: space-between;
            margin-top: 0.5rem;
        }

        .price-display {
            display: flex;
            flex-direction: column;
        }

        .current-price {
            font-size: 1.15rem;
            font-weight: 800;
            color: #ffffff;
        }

        .retail-strike {
            font-size: 0.72rem;
            color: var(--text-dim);
            text-decoration: line-through;
        }

        .owner-chip {
            font-size: 0.72rem;
            color: var(--text-muted);
            display: flex;
            align-items: center;
            gap: 0.3rem;
        }

        /* Tab 2: Filters Bar */
        .filter-bar {
            display: flex;
            flex-wrap: wrap;
            align-items: center;
            justify-content: space-between;
            gap: 1rem;
            background: var(--bg-card);
            border: 1px solid var(--border);
            border-radius: 12px;
            padding: 0.85rem 1.25rem;
            margin-bottom: 1.5rem;
        }

        .filter-group {
            display: flex;
            align-items: center;
            gap: 0.5rem;
        }

        .filter-pill {
            background: transparent;
            border: 1px solid var(--border);
            color: var(--text-muted);
            font-size: 0.78rem;
            font-weight: 600;
            padding: 0.4rem 0.85rem;
            border-radius: 8px;
            cursor: pointer;
            transition: all 0.15s ease;
        }

        .filter-pill.active {
            background: var(--accent);
            border-color: var(--accent);
            color: white;
        }

        /* Tab 3: Barter Loops Inspector */
        .loops-header {
            background: linear-gradient(135deg, rgba(139, 92, 246, 0.15), rgba(59, 130, 246, 0.1));
            border: 1px solid rgba(139, 92, 246, 0.3);
            border-radius: 16px;
            padding: 1.5rem 1.75rem;
            margin-bottom: 2rem;
            display: flex;
            align-items: center;
            justify-content: space-between;
        }

        .loops-header h2 {
            font-size: 1.35rem;
            font-weight: 800;
            color: #ffffff;
            margin-bottom: 0.3rem;
        }

        .loops-grid {
            display: flex;
            flex-direction: column;
            gap: 1.5rem;
        }

        .loop-card {
            background: var(--bg-card);
            border: 1px solid rgba(139, 92, 246, 0.4);
            border-radius: 16px;
            padding: 1.5rem;
            position: relative;
            overflow: hidden;
            box-shadow: 0 4px 20px rgba(0, 0, 0, 0.3);
        }

        .loop-card::before {
            content: '';
            position: absolute;
            top: 0;
            left: 0;
            width: 4px;
            height: 100%;
            background: linear-gradient(to bottom, #8b5cf6, #10b981);
        }

        .loop-card-top {
            display: flex;
            align-items: center;
            justify-content: space-between;
            margin-bottom: 1.25rem;
        }

        .loop-confidence {
            display: flex;
            align-items: center;
            gap: 0.5rem;
            background: rgba(139, 92, 246, 0.15);
            border: 1px solid rgba(139, 92, 246, 0.3);
            color: #c4b5fd;
            font-weight: 700;
            font-size: 0.8rem;
            padding: 0.35rem 0.85rem;
            border-radius: 9999px;
        }

        .loop-exchange-flow {
            display: grid;
            grid-template-columns: 1fr auto 1fr;
            align-items: center;
            gap: 1.5rem;
            margin-bottom: 1.5rem;
        }

        @media (max-width: 768px) {
            .loop-exchange-flow {
                grid-template-columns: 1fr;
            }
        }

        .student-loop-box {
            background: var(--bg-input);
            border: 1px solid var(--border);
            border-radius: 12px;
            padding: 1.1rem;
        }

        .student-loop-box .name {
            font-size: 0.95rem;
            font-weight: 700;
            color: white;
            margin-bottom: 0.2rem;
        }

        .student-loop-box .campus-id {
            font-size: 0.72rem;
            color: var(--text-dim);
            margin-bottom: 0.75rem;
        }

        .swap-item {
            font-size: 0.8rem;
            margin-bottom: 0.35rem;
            display: flex;
            align-items: center;
            gap: 0.4rem;
        }

        .swap-item .label {
            font-weight: 700;
            font-size: 0.7rem;
            padding: 0.15rem 0.4rem;
            border-radius: 4px;
        }

        .label-has { background: rgba(59, 130, 246, 0.2); color: #93c5fd; }
        .label-wants { background: rgba(16, 185, 129, 0.2); color: #6ee7b7; }

        .loop-center-arrow {
            display: flex;
            flex-direction: column;
            align-items: center;
            gap: 0.3rem;
            color: #a78bfa;
            font-size: 1.5rem;
            font-weight: 800;
        }

        .loop-center-arrow span {
            font-size: 0.68rem;
            text-transform: uppercase;
            letter-spacing: 0.08em;
            color: var(--text-dim);
            font-weight: 700;
        }

        .loop-footer {
            border-top: 1px solid var(--border);
            padding-top: 1rem;
            display: flex;
            align-items: center;
            justify-content: space-between;
        }

        .loop-benefits {
            font-size: 0.8rem;
            color: #34d399;
            font-weight: 600;
            display: flex;
            align-items: center;
            gap: 1rem;
        }

        /* Tab 4: Leaderboard */
        .leaderboard-table {
            width: 100%;
            background: var(--bg-card);
            border: 1px solid var(--border);
            border-radius: 14px;
            border-collapse: collapse;
            overflow: hidden;
        }

        .leaderboard-table th, .leaderboard-table td {
            padding: 1rem 1.25rem;
            text-align: left;
        }

        .leaderboard-table th {
            background: rgba(255, 255, 255, 0.02);
            font-size: 0.75rem;
            text-transform: uppercase;
            letter-spacing: 0.05em;
            color: var(--text-dim);
            font-weight: 700;
            border-bottom: 1px solid var(--border);
        }

        .leaderboard-table tr:not(:last-child) td {
            border-bottom: 1px solid var(--border);
        }

        .leaderboard-table tr:hover td {
            background: rgba(255, 255, 255, 0.02);
        }

        .rank-badge {
            width: 28px;
            height: 28px;
            border-radius: 50%;
            display: inline-flex;
            align-items: center;
            justify-content: center;
            font-weight: 800;
            font-size: 0.8rem;
        }

        .rank-1 { background: #fbbf24; color: #78350f; }
        .rank-2 { background: #94a3b8; color: #0f172a; }
        .rank-3 { background: #d97706; color: white; }
        .rank-other { background: var(--bg-input); color: var(--text-muted); }

        /* Modal Backdrop */
        .modal-backdrop {
            display: none;
            position: fixed;
            top: 0;
            left: 0;
            right: 0;
            bottom: 0;
            background: rgba(0, 0, 0, 0.75);
            backdrop-filter: blur(8px);
            z-index: 100;
            align-items: center;
            justify-content: center;
            padding: 1rem;
        }

        .modal-content {
            background: var(--bg-card);
            border: 1px solid var(--border);
            border-radius: 18px;
            width: 100%;
            max-width: 580px;
            max-height: 90vh;
            overflow-y: auto;
            box-shadow: 0 20px 40px rgba(0, 0, 0, 0.6);
            animation: modalPop 0.2s cubic-bezier(0.16, 1, 0.3, 1);
        }

        @keyframes modalPop {
            from { opacity: 0; transform: scale(0.95); }
            to { opacity: 1; transform: scale(1); }
        }

        .modal-header {
            padding: 1.25rem 1.5rem;
            border-bottom: 1px solid var(--border);
            display: flex;
            align-items: center;
            justify-content: space-between;
        }

        .modal-header h3 {
            font-size: 1.2rem;
            font-weight: 700;
        }

        .modal-close {
            background: transparent;
            border: none;
            color: var(--text-dim);
            font-size: 1.5rem;
            cursor: pointer;
            line-height: 1;
        }

        .modal-close:hover { color: white; }

        .modal-body {
            padding: 1.5rem;
        }

        .form-group {
            margin-bottom: 1rem;
        }

        .form-group label {
            display: block;
            font-size: 0.8rem;
            font-weight: 600;
            color: var(--text-muted);
            margin-bottom: 0.35rem;
        }

        .form-control {
            width: 100%;
            background: var(--bg-input);
            border: 1px solid var(--border);
            color: white;
            font-family: inherit;
            font-size: 0.85rem;
            padding: 0.65rem 0.85rem;
            border-radius: 8px;
            outline: none;
        }

        .form-control:focus {
            border-color: #3b82f6;
        }

        .form-row {
            display: grid;
            grid-template-columns: 1fr 1fr;
            gap: 1rem;
        }

        .receipt-box {
            font-family: 'JetBrains Mono', monospace;
            background: #060911;
            border: 1px solid #1e293b;
            color: #38bdf8;
            padding: 1.25rem;
            border-radius: 10px;
            font-size: 0.78rem;
            white-space: pre-wrap;
            line-height: 1.4;
            max-height: 400px;
            overflow-y: auto;
        }

        /* Toast Notifications */
        .toast-container {
            position: fixed;
            bottom: 2rem;
            right: 2rem;
            z-index: 200;
            display: flex;
            flex-direction: column;
            gap: 0.75rem;
        }

        .toast {
            background: #111827;
            border: 1px solid #10b981;
            color: white;
            padding: 0.85rem 1.25rem;
            border-radius: 10px;
            box-shadow: 0 10px 25px rgba(0, 0, 0, 0.4);
            display: flex;
            align-items: center;
            gap: 0.6rem;
            font-size: 0.85rem;
            font-weight: 600;
            animation: slideUp 0.25s ease;
        }

        @keyframes slideUp {
            from { opacity: 0; transform: translateY(15px); }
            to { opacity: 1; transform: translateY(0); }
        }
    </style>
</head>
<body>
    <div class="ambient-glow"></div>

    <!-- Header Navbar -->
    <header>
        <a href="/" class="brand">
            <div class="brand-icon">📚</div>
            <div class="brand-text">
                <h1>BookLoop AI</h1>
                <span>Campus Circular Economy</span>
            </div>
        </a>

        <div class="nav-right">
            <div class="live-pill">
                <div class="pulse-dot"></div>
                <span>Campus Network Live :8080</span>
            </div>

            <!-- Active User Switcher -->
            <div class="user-switcher-container">
                <div class="user-avatar" id="activeUserAvatar">AS</div>
                <div class="user-meta">
                    <span class="user-meta-name" id="activeUserName">Aarav Sharma</span>
                    <span class="user-meta-karma" id="activeUserKarma">🌱 120 Karma</span>
                </div>
                <select class="user-select" id="userSelectDropdown" onchange="switchActiveUser(this.value)">
                    <option value="">Switch Student...</option>
                </select>
            </div>

            <button class="btn btn-primary btn-sm" onclick="openAddBookModal()">
                <span>＋</span> List Textbook
            </button>
        </div>
    </header>

    <!-- Main Content Area -->
    <main>
        <!-- Impact Stats Ribbon -->
        <div class="impact-ribbon">
            <div class="stat-card">
                <div class="stat-icon green">💰</div>
                <div class="stat-info">
                    <div class="stat-num" id="statSavings">$0</div>
                    <div class="stat-label">Campus Dollars Saved</div>
                </div>
            </div>
            <div class="stat-card">
                <div class="stat-icon blue">🔄</div>
                <div class="stat-info">
                    <div class="stat-num" id="statReused">0</div>
                    <div class="stat-label">Textbooks Reused</div>
                </div>
            </div>
            <div class="stat-card">
                <div class="stat-icon purple">⚡</div>
                <div class="stat-info">
                    <div class="stat-num" id="statLoops">0</div>
                    <div class="stat-label">Active Barter Loops</div>
                </div>
            </div>
            <div class="stat-card">
                <div class="stat-icon amber">🌱</div>
                <div class="stat-info">
                    <div class="stat-num" id="statTrees">0</div>
                    <div class="stat-label">Community Trees Saved</div>
                </div>
            </div>
        </div>

        <!-- Tabs Navigation -->
        <div class="tabs-header">
            <button class="tab-btn active" onclick="switchTab('tab-search')">
                🔍 AI Smart Search
            </button>
            <button class="tab-btn" onclick="switchTab('tab-catalog')">
                📚 Textbook Catalog
            </button>
            <button class="tab-btn" onclick="switchTab('tab-loops')">
                ⚡ Reciprocal Barter Loops <span class="tab-badge" id="barterBadge">2</span>
            </button>
            <button class="tab-btn" onclick="switchTab('tab-leaderboard')">
                🏆 Eco-Karma Leaderboard
            </button>
            <button class="tab-btn" onclick="switchTab('tab-history')">
                📜 Transaction Log
            </button>
        </div>

        <!-- TAB 1: AI SMART SEARCH -->
        <div id="tab-search" class="tab-pane active">
            <div class="ai-search-hero">
                <h2><span>🤖</span> Natural Language Smart Intent Pipeline</h2>
                <p>
                    Search using conversational phrases like <i>"affordable beginner java book under $30"</i>, 
                    <i>"need operating systems for barter exchange"</i>, or <i>"free calculus copy"</i>. 
                    Our built-in semantic intent parser extracts budget constraints, subjects, and condition filters automatically.
                </p>

                <div class="search-input-wrapper">
                    <input type="text" id="aiSearchInput" class="search-input" 
                           placeholder="Type what you need (e.g. 'cheap DBMS textbook for CS', 'clean code under $40')..." 
                           onkeydown="if(event.key==='Enter') executeAiSearch()">
                    <button class="btn btn-primary" onclick="executeAiSearch()">
                        Search AI
                    </button>
                </div>

                <div class="quick-chips">
                    <span>Try asking:</span>
                    <button class="chip" onclick="setQuery('affordable beginner book for learning Java')">"affordable beginner book for learning Java"</button>
                    <button class="chip" onclick="setQuery('operating systems textbook under $20')">"operating systems textbook under $20"</button>
                    <button class="chip" onclick="setQuery('looking for database systems for exchange')">"looking for database systems for exchange"</button>
                    <button class="chip" onclick="setQuery('free physics or math donation')">"free physics or math donation"</button>
                </div>
            </div>

            <!-- Live Extracted Intent Display -->
            <div class="intent-card" id="intentCard">
                <div class="intent-title">🧠 AI Extracted Query Intent & Constraints</div>
                <div class="intent-grid">
                    <div class="intent-item"><span>Target Subject:</span> <b id="intentSubject">Computer Science</b></div>
                    <div class="intent-item"><span>Max Price Constraint:</span> <b id="intentBudget">$30.00</b></div>
                    <div class="intent-item"><span>Preferred Mode:</span> <b id="intentMode">ALL</b></div>
                    <div class="intent-item"><span>Condition Filter:</span> <b id="intentCondition">ANY</b></div>
                    <div class="intent-item"><span>Keywords:</span> <b id="intentKeywords">java, programming</b></div>
                </div>
            </div>

            <!-- Search Results Cards -->
            <div class="books-grid" id="searchResultsGrid">
                <!-- Dynamically populated -->
            </div>
        </div>

        <!-- TAB 2: TEXTBOOK CATALOG -->
        <div id="tab-catalog" class="tab-pane">
            <div class="filter-bar">
                <div class="filter-group">
                    <button class="filter-pill active" onclick="filterCatalog('ALL', this)">All Books</button>
                    <button class="filter-pill" onclick="filterCatalog('EXCHANGE', this)">🔁 Exchange Only</button>
                    <button class="filter-pill" onclick="filterCatalog('SELL', this)">💵 For Sale</button>
                    <button class="filter-pill" onclick="filterCatalog('DONATE', this)">🎁 Free / Donation</button>
                </div>
                <div class="filter-group">
                    <select id="subjectSelect" class="user-select" onchange="loadCatalog()">
                        <option value="ALL">All Subjects</option>
                        <option value="Computer Science">Computer Science</option>
                        <option value="Mathematics">Mathematics</option>
                        <option value="Electrical Engineering">Electrical Engineering</option>
                        <option value="Physics">Physics</option>
                        <option value="Business">Business</option>
                    </select>
                </div>
            </div>

            <div class="books-grid" id="catalogGrid">
                <!-- Dynamically populated -->
            </div>
        </div>

        <!-- TAB 3: BARTER LOOPS -->
        <div id="tab-loops" class="tab-pane">
            <div class="loops-header">
                <div>
                    <h2>⚡ Reciprocal Barter Loop Engine</h2>
                    <p style="color: var(--text-muted); font-size: 0.85rem;">
                        Automated 2-way graph cycle detection connects students with reciprocal needs for 100% zero-cash textbook swaps.
                    </p>
                </div>
                <button class="btn btn-secondary btn-sm" onclick="loadBarterLoops()">
                    🔄 Refresh Loops
                </button>
            </div>

            <div class="loops-grid" id="barterLoopsContainer">
                <!-- Dynamically populated -->
            </div>
        </div>

        <!-- TAB 4: LEADERBOARD -->
        <div id="tab-leaderboard" class="tab-pane">
            <h2 style="font-size: 1.4rem; font-weight: 800; margin-bottom: 0.5rem;">🌱 Campus Sustainability Leaderboard</h2>
            <p style="color: var(--text-muted); font-size: 0.85rem; margin-bottom: 1.5rem;">
                Students earn Eco-Karma points and increase their peer trust rating by donating textbooks and participating in circular barter loops.
            </p>

            <table class="leaderboard-table">
                <thead>
                    <tr>
                        <th style="width: 70px;">Rank</th>
                        <th>Student</th>
                        <th>Campus ID</th>
                        <th>Department</th>
                        <th>Eco-Karma</th>
                        <th>Trust Score</th>
                        <th>Books Shared</th>
                        <th>Savings Generated</th>
                    </tr>
                </thead>
                <tbody id="leaderboardBody">
                    <!-- Dynamically populated -->
                </tbody>
            </table>
        </div>

        <!-- TAB 5: TRANSACTION LOG -->
        <div id="tab-history" class="tab-pane">
            <h2 style="font-size: 1.4rem; font-weight: 800; margin-bottom: 0.5rem;">📜 Transaction Audit Receipts</h2>
            <p style="color: var(--text-muted); font-size: 0.85rem; margin-bottom: 1.5rem;">
                Immutable audit ledger recording all campus exchanges, peer sales, and donations.
            </p>
            <div id="transactionLogContainer" style="display: flex; flex-direction: column; gap: 1rem;">
                <!-- Dynamically populated -->
            </div>
        </div>
    </main>

    <!-- MODAL: ADD TEXTBOOK -->
    <div class="modal-backdrop" id="addBookModal">
        <div class="modal-content">
            <div class="modal-header">
                <h3>List a Textbook</h3>
                <button class="modal-close" onclick="closeModal('addBookModal')">&times;</button>
            </div>
            <div class="modal-body">
                <form id="addBookForm" onsubmit="submitNewBook(event)">
                    <div class="form-group">
                        <label>Book Title *</label>
                        <input type="text" class="form-control" name="title" required placeholder="e.g. Introduction to Algorithms (CLRS)">
                    </div>
                    <div class="form-row">
                        <div class="form-group">
                            <label>Author</label>
                            <input type="text" class="form-control" name="author" placeholder="e.g. Cormen, Leiserson">
                        </div>
                        <div class="form-group">
                            <label>Edition</label>
                            <input type="text" class="form-control" name="edition" placeholder="e.g. 4th Edition">
                        </div>
                    </div>
                    <div class="form-row">
                        <div class="form-group">
                            <label>Subject *</label>
                            <select class="form-control" name="subject" required>
                                <option value="Computer Science">Computer Science</option>
                                <option value="Mathematics">Mathematics</option>
                                <option value="Electrical Engineering">Electrical Engineering</option>
                                <option value="Physics">Physics</option>
                                <option value="Business">Business</option>
                            </select>
                        </div>
                        <div class="form-group">
                            <label>Physical Condition *</label>
                            <select class="form-control" name="condition" required>
                                <option value="LIKE_NEW">Like New</option>
                                <option value="GOOD" selected>Good</option>
                                <option value="NEW">Brand New</option>
                                <option value="FAIR">Fair</option>
                                <option value="POOR">Well-Loved</option>
                            </select>
                        </div>
                    </div>
                    <div class="form-row">
                        <div class="form-group">
                            <label>Listing Mode *</label>
                            <select class="form-control" name="mode" id="modalListingMode" onchange="handleModeChange(this.value)" required>
                                <option value="EXCHANGE">🔁 Barter / Exchange</option>
                                <option value="SELL">💵 For Sale</option>
                                <option value="DONATE">🎁 Free Eco-Donation</option>
                            </select>
                        </div>
                        <div class="form-group" id="priceFieldGroup" style="display:none;">
                            <label>Sale Price ($)</label>
                            <input type="number" step="0.5" class="form-control" name="price" value="0.0" min="0">
                        </div>
                    </div>
                    <div class="form-row" id="barterFieldsGroup">
                        <div class="form-group">
                            <label>Wanted Subject (for Barter)</label>
                            <input type="text" class="form-control" name="neededSubject" placeholder="e.g. Computer Science">
                        </div>
                        <div class="form-group">
                            <label>Specific Book Wanted</label>
                            <input type="text" class="form-control" name="neededBookTitle" placeholder="e.g. Head First Java">
                        </div>
                    </div>
                    <div style="margin-top: 1.5rem; display: flex; justify-content: flex-end; gap: 0.75rem;">
                        <button type="button" class="btn btn-secondary" onclick="closeModal('addBookModal')">Cancel</button>
                        <button type="submit" class="btn btn-primary">Create Listing</button>
                    </div>
                </form>
            </div>
        </div>
    </div>

    <!-- MODAL: TRANSACTION RECEIPT -->
    <div class="modal-backdrop" id="receiptModal">
        <div class="modal-content">
            <div class="modal-header">
                <h3>Transaction Receipt</h3>
                <button class="modal-close" onclick="closeModal('receiptModal')">&times;</button>
            </div>
            <div class="modal-body">
                <div class="receipt-box" id="receiptContent"></div>
                <div style="margin-top: 1.25rem; display: flex; justify-content: flex-end;">
                    <button class="btn btn-primary" onclick="closeModal('receiptModal')">Done</button>
                </div>
            </div>
        </div>
    </div>

    <!-- Toast Container -->
    <div class="toast-container" id="toastContainer"></div>

    <script>
        let currentActiveUser = null;
        let allStudents = [];
        let currentCatalogMode = 'ALL';

        // Initialize on page load
        document.addEventListener('DOMContentLoaded', () => {
            loadActiveUser();
            loadAllStudents();
            loadStats();
            executeAiSearch(); // Run default smart search
            loadCatalog();
            loadBarterLoops();
            loadLeaderboard();
            loadTransactions();
        });

        function showToast(message) {
            const container = document.getElementById('toastContainer');
            const toast = document.createElement('div');
            toast.className = 'toast';
            toast.innerHTML = `<span>✓</span> <div>${message}</div>`;
            container.appendChild(toast);
            setTimeout(() => {
                toast.style.opacity = '0';
                setTimeout(() => toast.remove(), 300);
            }, 3500);
        }

        function switchTab(tabId) {
            document.querySelectorAll('.tab-btn').forEach(btn => btn.classList.remove('active'));
            document.querySelectorAll('.tab-pane').forEach(p => p.classList.remove('active'));
            
            event.currentTarget.classList.add('active');
            const pane = document.getElementById(tabId);
            if (pane) pane.classList.add('active');

            if (tabId === 'tab-loops') loadBarterLoops();
            if (tabId === 'tab-leaderboard') loadLeaderboard();
            if (tabId === 'tab-history') loadTransactions();
            if (tabId === 'tab-catalog') loadCatalog();
        }

        async function loadActiveUser() {
            try {
                const res = await fetch('/api/active-user');
                currentActiveUser = await res.json();
                if (currentActiveUser) {
                    const initials = currentActiveUser.name.split(' ').map(n => n[0]).join('').substring(0, 2).toUpperCase();
                    document.getElementById('activeUserAvatar').textContent = initials;
                    document.getElementById('activeUserName').textContent = currentActiveUser.name;
                    document.getElementById('activeUserKarma').textContent = `🌱 ${currentActiveUser.karmaPoints} Karma (Trust: ${currentActiveUser.trustScore}★)`;
                }
            } catch (err) {
                console.error("Failed to load active user", err);
            }
        }

        async function loadAllStudents() {
            try {
                const res = await fetch('/api/users');
                allStudents = await res.json();
                const select = document.getElementById('userSelectDropdown');
                select.innerHTML = '<option value="">Switch Student...</option>';
                allStudents.forEach(s => {
                    const opt = document.createElement('option');
                    opt.value = s.id;
                    opt.textContent = `${s.name} (${s.campusId}) - ${s.karmaPoints} pts`;
                    select.appendChild(opt);
                });
            } catch (err) {
                console.error("Failed to load students", err);
            }
        }

        async function switchActiveUser(userId) {
            if (!userId) return;
            try {
                const res = await fetch('/api/active-user', {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify({ userId })
                });
                const updated = await res.json();
                showToast(`Switched active profile to ${updated.name}!`);
                await loadActiveUser();
                loadStats();
                loadBarterLoops();
            } catch (err) {
                console.error("Failed to switch user", err);
            }
        }

        async function loadStats() {
            try {
                const res = await fetch('/api/stats');
                const data = await res.json();
                document.getElementById('statSavings').textContent = `$${data.campusSavings.toFixed(2)}`;
                document.getElementById('statReused').textContent = data.booksReused;
                document.getElementById('statLoops').textContent = data.activeLoops;
                document.getElementById('statTrees').textContent = data.treesSaved;
                document.getElementById('barterBadge').textContent = data.activeLoops;
            } catch (err) {
                console.error("Failed to load stats", err);
            }
        }

        function setQuery(text) {
            document.getElementById('aiSearchInput').value = text;
            executeAiSearch();
        }

        async function executeAiSearch() {
            const query = document.getElementById('aiSearchInput').value || 'affordable beginner book for learning Java';
            const grid = document.getElementById('searchResultsGrid');
            grid.innerHTML = '<div style="color:var(--text-muted); padding:2rem;">Searching AI Semantic Pipeline...</div>';

            try {
                const res = await fetch(`/api/search?q=${encodeURIComponent(query)}`);
                const data = await res.json();

                // Display Intent breakdown
                const intent = data.intent;
                const card = document.getElementById('intentCard');
                if (intent) {
                    card.style.display = 'block';
                    document.getElementById('intentSubject').textContent = intent.targetSubject || 'Any Subject';
                    document.getElementById('intentBudget').textContent = intent.maxBudget ? `$${intent.maxBudget.toFixed(2)}` : 'No Cap';
                    document.getElementById('intentMode').textContent = intent.modeFilter || 'ALL';
                    document.getElementById('intentCondition').textContent = intent.conditionFilter || 'ANY';
                    document.getElementById('intentKeywords').textContent = (intent.keywords && intent.keywords.length > 0) ? intent.keywords.join(', ') : 'semantic';
                }

                // Render Scored Results
                grid.innerHTML = '';
                if (!data.results || data.results.length === 0) {
                    grid.innerHTML = '<div style="color:var(--text-muted); padding:2rem;">No matching textbooks found for this intent. Try another natural language query.</div>';
                    return;
                }

                data.results.forEach(m => {
                    const b = m.book;
                    const card = createBookCard(b, m.score, m.explanation);
                    grid.appendChild(card);
                });
            } catch (err) {
                grid.innerHTML = '<div style="color:#ef4444; padding:2rem;">Failed to fetch search results.</div>';
                console.error(err);
            }
        }

        function filterCatalog(mode, element) {
            document.querySelectorAll('.filter-pill').forEach(p => p.classList.remove('active'));
            if (element) element.classList.add('active');
            currentCatalogMode = mode;
            loadCatalog();
        }

        async function loadCatalog() {
            const grid = document.getElementById('catalogGrid');
            grid.innerHTML = '<div style="color:var(--text-muted); padding:2rem;">Loading inventory...</div>';

            try {
                const subject = document.getElementById('subjectSelect').value;
                let url = `/api/books?mode=${currentCatalogMode}&subject=${encodeURIComponent(subject)}`;
                const res = await fetch(url);
                const books = await res.json();

                grid.innerHTML = '';
                if (books.length === 0) {
                    grid.innerHTML = '<div style="color:var(--text-muted); padding:2rem;">No textbooks listed in this category yet.</div>';
                    return;
                }

                books.forEach(b => {
                    grid.appendChild(createBookCard(b));
                });
            } catch (err) {
                grid.innerHTML = '<div style="color:#ef4444; padding:2rem;">Failed to load catalog.</div>';
                console.error(err);
            }
        }

        function createBookCard(b, score = null, explanation = null) {
            const card = document.createElement('div');
            card.className = 'book-card';

            const modeClass = `mode-${b.mode}`;
            let scoreBadgeHtml = '';
            if (score !== null) {
                scoreBadgeHtml = `<div class="score-pill">${score.toFixed(0)}% MATCH</div>`;
            }

            let explanationHtml = '';
            if (explanation) {
                explanationHtml = `<div class="match-explanation">✨ ${explanation}</div>`;
            }

            let exchangeHtml = '';
            if (b.mode === 'EXCHANGE' && (b.neededBookTitle || b.neededSubject)) {
                exchangeHtml = `<div class="exchange-wants">🔁 Seeking: <b>${b.neededBookTitle || b.neededSubject}</b></div>`;
            }

            let priceText = b.mode === 'DONATE' ? 'FREE (🎁)' : (b.mode === 'EXCHANGE' ? 'BARTER (🔁)' : `$${b.price.toFixed(2)}`);

            card.innerHTML = `
                <div>
                    <div class="book-card-top">
                        <span class="badge-mode ${modeClass}">${b.modeLabel || b.mode}</span>
                        ${scoreBadgeHtml}
                    </div>
                    <div class="book-title">${b.title}</div>
                    <div class="book-author">by ${b.author} (${b.edition})</div>
                    <div class="book-meta-chips">
                        <span class="meta-chip">📚 ${b.subject}</span>
                        <span class="meta-chip">🏷️ ${b.conditionLabel}</span>
                    </div>
                    ${explanationHtml}
                    ${exchangeHtml}
                </div>
                <div class="book-footer">
                    <div class="price-display">
                        <span class="current-price">${priceText}</span>
                        ${b.originalRetailPrice > b.price ? `<span class="retail-strike">Retail: $${b.originalRetailPrice.toFixed(2)}</span>` : ''}
                    </div>
                    <div class="owner-chip">
                        <span>👤</span> ${b.ownerName}
                    </div>
                </div>
            `;
            return card;
        }

        async function loadBarterLoops() {
            const container = document.getElementById('barterLoopsContainer');
            container.innerHTML = '<div style="color:var(--text-muted); padding:2rem;">Detecting mutual barter cycles...</div>';

            try {
                const res = await fetch('/api/barter-loops');
                const loops = await res.json();

                container.innerHTML = '';
                if (loops.length === 0) {
                    container.innerHTML = `
                        <div style="background:var(--bg-card); border:1px solid var(--border); border-radius:14px; padding:3rem; text-align:center;">
                            <div style="font-size:2.5rem; margin-bottom:1rem;">🎉</div>
                            <h3 style="font-size:1.2rem; font-weight:700; margin-bottom:0.5rem;">All Barter Loops Completed!</h3>
                            <p style="color:var(--text-muted); max-width:480px; margin:0 auto;">
                                There are currently no unexecuted mutual loops. List a new book or add to your wishlist to trigger new AI pairings!
                            </p>
                        </div>
                    `;
                    return;
                }

                loops.forEach(l => {
                    const card = document.createElement('div');
                    card.className = 'loop-card';
                    card.innerHTML = `
                        <div class="loop-card-top">
                            <div class="loop-confidence">
                                <span>⚡</span> Mutual Match Confidence: <b>${l.matchScore}%</b>
                            </div>
                            <span style="font-size:0.75rem; color:var(--text-dim);">Reciprocal 2-Way Loop</span>
                        </div>
                        <div class="loop-exchange-flow">
                            <div class="student-loop-box">
                                <div class="name">👤 ${l.studentA.name}</div>
                                <div class="campus-id">${l.studentA.campusId} • ${l.studentA.department}</div>
                                <div class="swap-item">
                                    <span class="label label-has">HAS</span>
                                    <span>${l.bookA.title}</span>
                                </div>
                                <div class="swap-item">
                                    <span class="label label-wants">WANTS</span>
                                    <span>${l.bookA.neededBookTitle || l.bookA.neededSubject}</span>
                                </div>
                            </div>

                            <div class="loop-center-arrow">
                                <span>ZERO CASH</span>
                                ⇄
                                <span>SWAP</span>
                            </div>

                            <div class="student-loop-box">
                                <div class="name">👤 ${l.studentB.name}</div>
                                <div class="campus-id">${l.studentB.campusId} • ${l.studentB.department}</div>
                                <div class="swap-item">
                                    <span class="label label-has">HAS</span>
                                    <span>${l.bookB.title}</span>
                                </div>
                                <div class="swap-item">
                                    <span class="label label-wants">WANTS</span>
                                    <span>${l.bookB.neededBookTitle || l.bookB.neededSubject}</span>
                                </div>
                            </div>
                        </div>

                        <div class="loop-footer">
                            <div class="loop-benefits">
                                <span>💰 $0.00 Cash Exchanged</span>
                                <span>🌱 +60 Eco-Karma Each</span>
                            </div>
                            <button class="btn btn-primary" onclick="executeBarter('${l.bookA.id}', '${l.bookB.id}')">
                                ⚡ Execute Barter Loop
                            </button>
                        </div>
                    `;
                    container.appendChild(card);
                });
            } catch (err) {
                container.innerHTML = '<div style="color:#ef4444; padding:2rem;">Failed to detect barter loops.</div>';
                console.error(err);
            }
        }

        async function executeBarter(bookAId, bookBId) {
            try {
                const res = await fetch('/api/barter-execute', {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify({ bookAId, bookBId })
                });
                const data = await res.json();
                if (data.success) {
                    showToast('Barter executed successfully! Eco-karma points awarded.');
                    document.getElementById('receiptContent').textContent = data.receipt;
                    openModal('receiptModal');
                    loadStats();
                    loadBarterLoops();
                    loadActiveUser();
                    loadLeaderboard();
                    loadTransactions();
                } else {
                    alert('Error executing barter: ' + (data.error || 'Failed'));
                }
            } catch (err) {
                console.error('Failed to execute barter', err);
                alert('Network error executing barter');
            }
        }

        async function loadLeaderboard() {
            const tbody = document.getElementById('leaderboardBody');
            tbody.innerHTML = '<tr><td colspan="8" style="text-align:center; color:var(--text-muted);">Loading standings...</td></tr>';

            try {
                const res = await fetch('/api/leaderboard');
                const users = await res.json();

                tbody.innerHTML = '';
                users.forEach((u, index) => {
                    const tr = document.createElement('tr');
                    const rank = index + 1;
                    const rankClass = rank === 1 ? 'rank-1' : (rank === 2 ? 'rank-2' : (rank === 3 ? 'rank-3' : 'rank-other'));

                    tr.innerHTML = `
                        <td><div class="rank-badge ${rankClass}">${rank}</div></td>
                        <td style="font-weight:700; color:white;">${u.name}</td>
                        <td style="color:var(--text-muted); font-size:0.8rem;">${u.campusId}</td>
                        <td style="font-size:0.8rem;">${u.department}</td>
                        <td style="color:#34d399; font-weight:700;">🌱 ${u.karmaPoints} pts</td>
                        <td style="color:#fbbf24; font-weight:600;">${u.trustScore} ★</td>
                        <td>${u.booksShared} shared</td>
                        <td style="color:#60a5fa; font-weight:600;">$${u.totalSavings.toFixed(2)}</td>
                    `;
                    tbody.appendChild(tr);
                });
            } catch (err) {
                tbody.innerHTML = '<tr><td colspan="8" style="color:#ef4444; text-align:center;">Failed to load leaderboard.</td></tr>';
                console.error(err);
            }
        }

        async function loadTransactions() {
            const container = document.getElementById('transactionLogContainer');
            try {
                const res = await fetch('/api/transactions');
                const txs = await res.json();

                container.innerHTML = '';
                if (txs.length === 0) {
                    container.innerHTML = '<div style="color:var(--text-muted); padding:2rem;">No transactions recorded yet. Execute a barter loop or sale to generate an audit receipt!</div>';
                    return;
                }

                txs.slice().reverse().forEach(t => {
                    const box = document.createElement('div');
                    box.className = 'receipt-box';
                    box.textContent = t.receipt || JSON.stringify(t, null, 2);
                    container.appendChild(box);
                });
            } catch (err) {
                container.innerHTML = '<div style="color:#ef4444; padding:2rem;">Failed to load transactions.</div>';
                console.error(err);
            }
        }

        function handleModeChange(mode) {
            const priceGroup = document.getElementById('priceFieldGroup');
            const barterGroup = document.getElementById('barterFieldsGroup');
            if (mode === 'SELL') {
                priceGroup.style.display = 'block';
                barterGroup.style.display = 'none';
            } else if (mode === 'EXCHANGE') {
                priceGroup.style.display = 'none';
                barterGroup.style.display = 'grid';
            } else {
                priceGroup.style.display = 'none';
                barterGroup.style.display = 'none';
            }
        }

        async function submitNewBook(e) {
            e.preventDefault();
            const form = e.target;
            const formData = new FormData(form);
            const payload = Object.fromEntries(formData.entries());

            try {
                const res = await fetch('/api/books', {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify(payload)
                });
                const result = await res.json();
                closeModal('addBookModal');
                form.reset();
                showToast(`Listed "${result.title}" successfully!`);
                loadCatalog();
                loadStats();
                loadBarterLoops();
            } catch (err) {
                console.error('Failed to add book', err);
                alert('Error listing book');
            }
        }

        function openAddBookModal() { openModal('addBookModal'); }
        function openModal(id) {
            const modal = document.getElementById(id);
            if (modal) modal.style.display = 'flex';
        }
        function closeModal(id) {
            const modal = document.getElementById(id);
            if (modal) modal.style.display = 'none';
        }
    </script>
</body>
</html>
""";
    }
}
