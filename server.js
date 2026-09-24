const http = require('http');
const fs = require('fs');
const path = require('path');

const PORT = 3000;
const DRAWABLE_DIR = path.join(__dirname, 'app/src/main/res/drawable');
const APK_PATH = path.join(__dirname, 'app/build/outputs/apk/debug/app-debug.apk');

// Helper to serve files
function serveStaticFile(filePath, contentType, res) {
  fs.readFile(filePath, (err, data) => {
    if (err) {
      res.writeHead(404, { 'Content-Type': 'text/plain' });
      res.end('File Not Found');
    } else {
      res.writeHead(200, {
        'Content-Type': contentType,
        'Cache-Control': 'public, max-age=86400'
      });
      res.end(data);
    }
  });
}

const server = http.createServer((req, res) => {
  const parsedUrl = new URL(req.url, `http://${req.headers.host || 'localhost'}`);
  const pathname = parsedUrl.pathname;

  // Serve drawable images
  if (pathname.startsWith('/assets/')) {
    const filename = pathname.replace('/assets/', '');
    const safePath = path.join(DRAWABLE_DIR, path.basename(filename));
    if (fs.existsSync(safePath)) {
      const ext = path.extname(safePath).toLowerCase();
      const mime = ext === '.png' ? 'image/png' : 'image/jpeg';
      return serveStaticFile(safePath, mime, res);
    }
  }

  // Serve Android APK
  if (pathname === '/download-apk') {
    if (fs.existsSync(APK_PATH)) {
      res.writeHead(200, {
        'Content-Type': 'application/vnd.android.package-archive',
        'Content-Disposition': 'attachment; filename="AURA-Royale.apk"'
      });
      return fs.createReadStream(APK_PATH).pipe(res);
    } else {
      res.writeHead(404, { 'Content-Type': 'text/plain' });
      return res.end('APK build in progress or not found.');
    }
  }

  // Health endpoint for dev server checks
  if (pathname === '/health' || pathname === '/api/health') {
    res.writeHead(200, { 'Content-Type': 'application/json' });
    return res.end(JSON.stringify({ status: 'ok', server: 'AURA Royale Web Dev Server', port: PORT }));
  }

  // Serve the complete interactive AURA Royale Web Application
  res.writeHead(200, {
    'Content-Type': 'text/html; charset=utf-8',
    'Cache-Control': 'no-cache'
  });
  res.end(getAppHtml());
});

server.listen(PORT, '0.0.0.0', () => {
  console.log(`[AURA Royale] Dev Server listening on http://0.0.0.0:${PORT}`);
});

function getAppHtml() {
  return `<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">
  <title>AURA Royale - Worldwide Creator Network</title>
  <meta name="description" content="Next-gen creator social ecosystem combining Facebook, Instagram, and WhatsApp with 4K video studio, real-time voice & video calling, and blockchain monetization.">
  <!-- Tailwind CSS -->
  <script src="https://cdn.tailwindcss.com"></script>
  <script>
    tailwind.config = {
      darkMode: 'class',
      theme: {
        extend: {
          colors: {
            obsidian: {
              bg: '#090A0F',
              surface: '#11141D',
              card: '#161A26',
              elevated: '#1D2233',
              border: '#262C40'
            },
            gold: {
              light: '#FFE680',
              primary: '#FFC72C',
              accent: '#E5A910',
              dark: '#B37D06',
              muted: 'rgba(255, 199, 44, 0.2)'
            }
          }
        }
      }
    }
  </script>
  <!-- Lucide Icons -->
  <script src="https://unpkg.com/lucide@latest"></script>
  <!-- Canvas Confetti -->
  <script src="https://cdn.jsdelivr.net/npm/canvas-confetti@1.9.4/dist/confetti.browser.min.js"></script>
  <style>
    @import url('https://fonts.googleapis.com/css2?family=Plus+Jakarta+Sans:wght@300;400;500;600;700;800&family=Cabinet+Grotesk:wght@700;800;900&family=JetBrains+Mono:wght@400;600&display=swap');
    body {
      font-family: 'Plus Jakarta Sans', sans-serif;
      background-color: #090A0F;
      color: #F8FAFC;
      user-select: none;
      -webkit-font-smoothing: antialiased;
    }
    .font-brand {
      font-family: 'Cabinet Grotesk', sans-serif;
    }
    .font-mono {
      font-family: 'JetBrains Mono', monospace;
    }
    .gold-gradient-text {
      background: linear-gradient(135deg, #FFF1B8 0%, #FFC72C 50%, #D99B00 100%);
      -webkit-background-clip: text;
      -webkit-text-fill-color: transparent;
    }
    .gold-border-glow {
      box-shadow: 0 0 15px rgba(255, 199, 44, 0.25);
    }
    /* Hide scrollbars */
    ::-webkit-scrollbar { display: none; }
    * { -ms-overflow-style: none; scrollbar-width: none; }
  </style>
</head>
<body class="bg-obsidian-bg text-slate-100 min-h-screen flex justify-center">

  <!-- Mobile/Tablet Container (Max-width 480px on desktop for app feel, full on mobile) -->
  <div class="w-full max-w-md bg-obsidian-bg min-h-screen flex flex-col relative border-x border-obsidian-border shadow-2xl">

    <!-- Top App Bar -->
    <header class="sticky top-0 z-40 bg-obsidian-surface/95 backdrop-blur-md border-b border-obsidian-border px-4 py-2.5 flex items-center justify-between">
      <div class="flex items-center gap-2.5">
        <div class="relative w-9 h-9 rounded-full ring-2 ring-gold-primary overflow-hidden bg-black flex-shrink-0">
          <img src="/assets/golden_logo_icon_1790280881946.jpg" alt="AURA Logo" class="w-full h-full object-cover" onerror="this.src='https://api.dicebear.com/7.x/identicon/svg?seed=AURA'">
        </div>
        <div>
          <div class="flex items-center gap-1.5 leading-none">
            <span class="font-brand text-xl font-black tracking-wider text-gold-primary">AURA</span>
            <span class="text-[9px] font-bold px-1.5 py-0.5 rounded bg-obsidian-elevated text-gold-light border border-gold-muted uppercase">Royale</span>
          </div>
          <p class="text-[10px] text-slate-400 font-medium">Worldwide Creator Network</p>
        </div>
      </div>

      <!-- Action Controls -->
      <div class="flex items-center gap-2">
        <button onclick="openSearchModal()" class="w-8 h-8 rounded-full bg-obsidian-card flex items-center justify-center text-slate-300 hover:text-gold-primary transition">
          <i data-lucide="search" class="w-4 h-4"></i>
        </button>

        <!-- Web3 Wallet Pill -->
        <button onclick="switchTab('vault')" class="flex items-center gap-1.5 px-2.5 py-1.5 rounded-full bg-obsidian-elevated border border-gold-muted hover:border-gold-primary transition">
          <span class="w-2 h-2 rounded-full bg-emerald-400 animate-pulse"></span>
          <span id="top-wallet-balance" class="text-xs font-semibold text-gold-light font-mono tabular-nums">$8,450 USDC</span>
        </button>

        <button onclick="openNotificationsModal()" class="relative w-8 h-8 rounded-full bg-obsidian-card flex items-center justify-center text-slate-300 hover:text-gold-primary transition">
          <i data-lucide="bell" class="w-4 h-4"></i>
          <span class="absolute top-1 right-1 w-2 h-2 rounded-full bg-red-500"></span>
        </button>
      </div>
    </header>

    <!-- Main Content Area (Dynamic Tabs) -->
    <main id="app-container" class="flex-1 pb-24 overflow-y-auto">
      <!-- Injected dynamically via JavaScript -->
    </main>

    <!-- Bottom Navigation Bar -->
    <nav class="fixed bottom-0 z-40 w-full max-w-md bg-obsidian-surface/95 backdrop-blur-md border-t border-obsidian-border px-3 py-1.5 flex items-center justify-around">
      <button onclick="switchTab('feed')" id="nav-btn-feed" class="flex flex-col items-center gap-1 p-1 text-gold-primary transition">
        <i data-lucide="play-circle" class="w-5 h-5"></i>
        <span class="text-[10px] font-bold">Feed</span>
      </button>

      <button onclick="switchTab('studio')" id="nav-btn-studio" class="flex flex-col items-center gap-1 p-1 text-slate-400 hover:text-gold-primary transition">
        <div class="w-9 h-9 rounded-full bg-gold-primary text-obsidian-bg flex items-center justify-center shadow-lg shadow-gold-primary/20">
          <i data-lucide="video" class="w-4 h-4 font-bold"></i>
        </div>
        <span class="text-[10px] font-medium">Studio</span>
      </button>

      <button onclick="switchTab('chat')" id="nav-btn-chat" class="relative flex flex-col items-center gap-1 p-1 text-slate-400 hover:text-gold-primary transition">
        <i data-lucide="message-square" class="w-5 h-5"></i>
        <span class="absolute top-0 right-2 w-2 h-2 rounded-full bg-red-500"></span>
        <span class="text-[10px] font-medium">Chat & Calls</span>
      </button>

      <button onclick="switchTab('analytics')" id="nav-btn-analytics" class="flex flex-col items-center gap-1 p-1 text-slate-400 hover:text-gold-primary transition">
        <i data-lucide="bar-chart-2" class="w-5 h-5"></i>
        <span class="text-[10px] font-medium">Analytics</span>
      </button>

      <button onclick="switchTab('vault')" id="nav-btn-vault" class="flex flex-col items-center gap-1 p-1 text-slate-400 hover:text-gold-primary transition">
        <i data-lucide="wallet" class="w-5 h-5"></i>
        <span class="text-[10px] font-medium">Vault</span>
      </button>
    </nav>

    <!-- Modals Container -->
    <div id="modal-container"></div>
  </div>

  <!-- Client Application Script -->
  <script>
    // State Store
    const state = {
      currentTab: 'feed',
      feedFilter: 'all',
      wallet: {
        ethAddress: '0x71C8392F8A4b29C4...614A',
        usdcBalance: 8450,
        auraBalance: 14250,
        ethBalance: 5.42
      },
      posts: [
        {
          id: 'post_1',
          creator: {
            name: 'Elena Vance',
            handle: '@elenavance',
            avatar: '/assets/creator_avatar_elena_1790280911154.jpg',
            verified: true,
            isLive: true
          },
          caption: 'Tokyo Night Reverie in 4K HDR. Shot with anamorphic primes & color graded in AURA Studio using Cinema Gold LUT. What do you think of this color palette? 🌆✨',
          tags: ['#TokyoNeo', '#CinemaGold', '#AURAWorldwide'],
          media: '/assets/sample_video_fashion_1790280956373.jpg',
          aspectRatio: '9:16',
          music: 'Tokyo Neon Drift · Synthwave (Original Audio)',
          likes: 28410,
          fires: 9420,
          trophies: 1850,
          comments: 1420,
          tipsUsd: 1450,
          isTokenGated: false,
          userLiked: false,
          timeAgo: '18m ago'
        },
        {
          id: 'post_2',
          creator: {
            name: 'Marcus Thorne',
            handle: '@marcusthorne',
            avatar: '/assets/creator_avatar_marcus_1790280929238.jpg',
            verified: true,
            isLive: false
          },
          caption: 'Golden Sunset across the Pacific Metropolis. 8K Aerial Hyperlapse captured from 1,200ft. Full BTS workflow available for Gold Pass subscribers! 🎬🚁',
          tags: ['#DroneCinema', '#GoldenHour', '#FilmMaking'],
          media: '/assets/sample_video_cinema_1790280942452.jpg',
          aspectRatio: '16:9',
          music: 'Golden Horizon Symphony · Marcus Thorne',
          likes: 45900,
          fires: 14200,
          trophies: 4890,
          comments: 2830,
          tipsUsd: 3890,
          isTokenGated: true,
          tokenTier: 'Gold Inner Circle Pass',
          userLiked: true,
          timeAgo: '2h ago'
        }
      ],
      stories: [
        { id: 's1', name: 'Elena Vance', avatar: '/assets/creator_avatar_elena_1790280911154.jpg', media: '/assets/sample_video_fashion_1790280956373.jpg', caption: 'Live backstage at Tokyo Neo-Fashion Week ✨', isLive: true },
        { id: 's2', name: 'Marcus Thorne', avatar: '/assets/creator_avatar_marcus_1790280929238.jpg', media: '/assets/sample_video_cinema_1790280942452.jpg', caption: 'Sunset drone master shots over the harbor 🌅', isLive: false }
      ],
      chatMessages: [
        { id: 'm1', sender: 'Elena Vance', text: 'Hey Aria! Loving the color grading on your latest Tokyo runway cut! Which LUT did you apply from the AURA suite?', time: '12:35 PM', isMe: false },
        { id: 'm2', sender: 'Me', text: 'Thank you Elena! I used the Cinema Gold preset with 45% warm highlight diffusion and dialed the anamorphic flare to 1.2x.', time: '12:38 PM', isMe: true },
        { id: 'm3', sender: 'Me', text: 'Sent 50 USDC direct tip for your masterclass advice! 🪙', time: '12:39 PM', isMe: true, isTip: true, tipAmount: 50 },
        { id: 'm4', sender: 'Elena Vance', text: null, time: '12:44 PM', isMe: false, isVoice: true, duration: 18 }
      ],
      activeCall: null,
      recordingVoice: false,
      recordingSec: 0,
      recordingTimer: null
    };

    // Tab Navigation
    function switchTab(tab) {
      state.currentTab = tab;
      ['feed', 'studio', 'chat', 'analytics', 'vault'].forEach(t => {
        const btn = document.getElementById('nav-btn-' + t);
        if (btn) {
          if (t === tab) {
            btn.className = 'flex flex-col items-center gap-1 p-1 text-gold-primary transition';
          } else {
            btn.className = 'flex flex-col items-center gap-1 p-1 text-slate-400 hover:text-gold-primary transition';
          }
        }
      });
      renderCurrentTab();
    }

    function renderCurrentTab() {
      const container = document.getElementById('app-container');
      if (state.currentTab === 'feed') renderFeed(container);
      else if (state.currentTab === 'studio') renderStudio(container);
      else if (state.currentTab === 'chat') renderChat(container);
      else if (state.currentTab === 'analytics') renderAnalytics(container);
      else if (state.currentTab === 'vault') renderVault(container);
      lucide.createIcons();
    }

    // 1. FEED RENDERER
    function renderFeed(container) {
      let filteredPosts = state.posts;
      if (state.feedFilter === 'gated') filteredPosts = state.posts.filter(p => p.isTokenGated);
      if (state.feedFilter === 'trending') filteredPosts = [...state.posts].sort((a,b) => b.likes - a.likes);

      container.innerHTML = \`
        <!-- Stories Carousel -->
        <div class="px-4 py-3 flex gap-3.5 overflow-x-auto border-b border-obsidian-border/50">
          <div onclick="switchTab('studio')" class="flex flex-col items-center gap-1 cursor-pointer flex-shrink-0">
            <div class="relative w-15 h-15 rounded-full p-0.5 ring-2 ring-gold-primary bg-obsidian-card">
              <img src="/assets/creator_avatar_elena_1790280911154.jpg" class="w-full h-full rounded-full object-cover">
              <div class="absolute bottom-0 right-0 w-5 h-5 rounded-full bg-gold-primary text-obsidian-bg flex items-center justify-center font-bold text-xs">+</div>
            </div>
            <span class="text-[11px] text-slate-200 font-medium">Your Story</span>
          </div>
          \${state.stories.map((s, idx) => \`
            <div onclick="openStoryViewer(\${idx})" class="flex flex-col items-center gap-1 cursor-pointer flex-shrink-0">
              <div class="relative w-15 h-15 rounded-full p-0.5 ring-2 ring-gold-accent bg-obsidian-card">
                <img src="\${s.avatar}" class="w-full h-full rounded-full object-cover">
                \${s.isLive ? '<span class="absolute -bottom-1 left-1/2 -translate-x-1/2 text-[8px] bg-red-600 text-white font-extrabold px-1 rounded uppercase">LIVE</span>' : ''}
              </div>
              <span class="text-[11px] text-slate-300 font-medium">\${s.name.split(' ')[0]}</span>
            </div>
          \`).join('')}
        </div>

        <!-- Filter Segmented Tabs -->
        <div class="px-4 py-2.5 flex gap-2 overflow-x-auto">
          \${[
            { id: 'all', label: 'Worldwide Viral' },
            { id: 'trending', label: 'Top Trending' },
            { id: 'gated', label: 'VIP Gated Passes' }
          ].map(f => \`
            <button onclick="setFeedFilter('\${f.id}')" class="px-3.5 py-1.5 rounded-full text-xs font-semibold whitespace-nowrap transition \${state.feedFilter === f.id ? 'bg-gold-primary text-obsidian-bg font-bold' : 'bg-obsidian-elevated text-slate-300 border border-obsidian-border'}">
              \${f.label}
            </button>
          \`).join('')}
        </div>

        <!-- Video Feed Cards -->
        <div class="flex flex-col gap-4 px-3 py-2">
          \${filteredPosts.map(p => \`
            <div class="bg-obsidian-card border border-obsidian-border rounded-2xl overflow-hidden shadow-lg">
              <!-- Creator Lockup -->
              <div class="p-3 flex items-center justify-between">
                <div class="flex items-center gap-2.5">
                  <img src="\${p.creator.avatar}" class="w-10 h-10 rounded-full object-cover ring-1 ring-gold-primary">
                  <div>
                    <div class="flex items-center gap-1">
                      <span class="text-sm font-bold text-slate-100">\${p.creator.name}</span>
                      <i data-lucide="check-circle" class="w-3.5 h-3.5 text-gold-primary fill-gold-primary/20"></i>
                    </div>
                    <span class="text-[11px] text-slate-400">\${p.creator.handle} · \${p.timeAgo}</span>
                  </div>
                </div>
                <div class="flex items-center gap-2">
                  <button onclick="openTipModal('\${p.creator.name}', '\${p.creator.avatar}')" class="flex items-center gap-1 px-3 py-1 rounded-full bg-obsidian-elevated border border-gold-primary text-gold-light hover:bg-gold-primary hover:text-obsidian-bg transition text-xs font-bold">
                    <i data-lucide="zap" class="w-3.5 h-3.5"></i>
                    <span>Tip</span>
                  </button>
                </div>
              </div>

              <!-- Video Media Container -->
              <div class="relative w-full \${p.aspectRatio === '16:9' ? 'aspect-video' : 'aspect-[4/5]'} bg-black overflow-hidden group">
                <img src="\${p.media}" class="w-full h-full object-cover">
                <div class="absolute inset-0 bg-gradient-to-t from-black/80 via-transparent to-black/30"></div>
                
                <!-- Aspect Ratio & Resolution Badge -->
                <div class="absolute top-3 right-3 px-2 py-0.5 rounded bg-black/60 backdrop-blur-md text-[10px] font-mono text-gold-light font-bold">
                  4K HDR · \${p.aspectRatio}
                </div>

                <!-- Token-Gated Overlay if locked -->
                \${p.isTokenGated ? \`
                  <div class="absolute inset-0 bg-black/75 backdrop-blur-sm flex flex-col items-center justify-center p-6 text-center gap-2">
                    <div class="w-12 h-12 rounded-full bg-gold-primary text-obsidian-bg flex items-center justify-center shadow-lg">
                      <i data-lucide="lock" class="w-6 h-6"></i>
                    </div>
                    <h4 class="text-base font-bold text-gold-light">Exclusive VIP Director's Cut</h4>
                    <p class="text-xs text-slate-300">Requires \${p.tokenTier} to stream raw 4K uncompressed cut</p>
                    <button onclick="switchTab('vault')" class="mt-2 px-4 py-2 rounded-xl bg-gold-primary text-obsidian-bg font-bold text-xs hover:bg-gold-light transition shadow-md">
                      Unlock with Web3 Pass
                    </button>
                  </div>
                \` : \`
                  <!-- Play Button -->
                  <div class="absolute inset-0 flex items-center justify-center">
                    <div class="w-12 h-12 rounded-full bg-black/50 border border-gold-light/60 flex items-center justify-center text-gold-primary cursor-pointer hover:scale-110 transition">
                      <i data-lucide="play" class="w-6 h-6 fill-gold-primary ml-0.5"></i>
                    </div>
                  </div>
                \`}

                <!-- Music Audio Pill -->
                <div class="absolute bottom-3 left-3 flex items-center gap-1.5 px-2.5 py-1 rounded-full bg-black/70 backdrop-blur-md text-[10px] text-slate-200">
                  <i data-lucide="music" class="w-3 h-3 text-gold-primary"></i>
                  <span>\${p.music}</span>
                </div>
              </div>

              <!-- Reactions & Action Bar (FB + Insta amalgam) -->
              <div class="p-3">
                <div class="flex items-center justify-between">
                  <div class="flex items-center gap-4">
                    <!-- Heart -->
                    <button onclick="toggleLike('\${p.id}')" class="flex items-center gap-1 text-xs font-semibold \${p.userLiked ? 'text-red-500' : 'text-slate-300'}">
                      <i data-lucide="heart" class="w-5 h-5 \${p.userLiked ? 'fill-red-500 text-red-500' : ''}"></i>
                      <span>\${p.likes.toLocaleString()}</span>
                    </button>
                    <!-- Fire -->
                    <button onclick="reactPost('\${p.id}', 'fire')" class="flex items-center gap-1 text-xs font-semibold text-slate-300 hover:text-amber-400">
                      <span>🔥</span>
                      <span>\${p.fires.toLocaleString()}</span>
                    </button>
                    <!-- Trophy -->
                    <button onclick="reactPost('\${p.id}', 'trophy')" class="flex items-center gap-1 text-xs font-semibold text-slate-300 hover:text-gold-primary">
                      <span>🏆</span>
                      <span>\${p.trophies.toLocaleString()}</span>
                    </button>
                    <!-- Comments -->
                    <button onclick="openCommentsModal('\${p.id}')" class="flex items-center gap-1 text-xs font-semibold text-slate-300">
                      <i data-lucide="message-circle" class="w-5 h-5"></i>
                      <span>\${p.comments}</span>
                    </button>
                  </div>
                  <!-- Total Tipped -->
                  <div class="flex items-center gap-1 px-2.5 py-1 rounded-lg bg-obsidian-elevated text-xs font-bold text-gold-light border border-gold-muted font-mono">
                    <span>🪙</span>
                    <span>$\${p.tipsUsd} Tipped</span>
                  </div>
                </div>

                <!-- Caption & Tags -->
                <p class="mt-2.5 text-xs text-slate-200 leading-relaxed">\${p.caption}</p>
                <div class="mt-1.5 flex gap-1.5">
                  \${p.tags.map(t => \`<span class="text-xs font-semibold text-gold-primary">\${t}</span>\`).join('')}
                </div>
              </div>
            </div>
          \`).join('')}
        </div>
      \`;
    }

    function setFeedFilter(filter) {
      state.feedFilter = filter;
      renderCurrentTab();
    }

    function toggleLike(postId) {
      const p = state.posts.find(item => item.id === postId);
      if (p) {
        p.userLiked = !p.userLiked;
        p.likes += p.userLiked ? 1 : -1;
        renderCurrentTab();
      }
    }

    function reactPost(postId, type) {
      const p = state.posts.find(item => item.id === postId);
      if (p) {
        if (type === 'fire') p.fires++;
        if (type === 'trophy') p.trophies++;
        renderCurrentTab();
      }
    }

    // 2. CREATOR VIDEO STUDIO RENDERER
    function renderStudio(container) {
      container.innerHTML = \`
        <div class="p-4 flex flex-col gap-4">
          <!-- Studio Header -->
          <div class="flex items-center justify-between">
            <div>
              <h2 class="text-lg font-bold text-gold-light">AURA Video Studio</h2>
              <p class="text-xs text-slate-400">4K HDR Multi-Track Editor</p>
            </div>
            <!-- Aspect Ratio Switcher -->
            <div class="flex bg-obsidian-card p-1 rounded-xl border border-obsidian-border">
              <button onclick="setStudioAspect('9:16')" class="px-2.5 py-1 rounded-lg text-xs font-bold bg-gold-primary text-obsidian-bg">9:16</button>
              <button onclick="setStudioAspect('1:1')" class="px-2.5 py-1 rounded-lg text-xs font-medium text-slate-300">1:1</button>
              <button onclick="setStudioAspect('16:9')" class="px-2.5 py-1 rounded-lg text-xs font-medium text-slate-300">16:9</button>
            </div>
          </div>

          <!-- Video Canvas Stage -->
          <div id="video-stage" class="relative w-full aspect-[9/16] bg-black rounded-2xl overflow-hidden border-2 border-gold-primary shadow-2xl flex items-center justify-center">
            <img id="stage-preview" src="/assets/sample_video_fashion_1790280956373.jpg" class="w-full h-full object-cover transition filter">
            <div class="absolute inset-0 bg-gradient-to-t from-black/80 via-transparent to-black/30"></div>

            <!-- Active Filter Badge -->
            <div class="absolute top-3 left-3 px-2 py-0.5 rounded bg-gold-primary text-obsidian-bg font-bold text-[10px] uppercase">
              LUT: <span id="active-lut-name">Cinema Gold</span>
            </div>

            <!-- Animated Subtitles Preview -->
            <div class="absolute bottom-12 inset-x-4 flex justify-center text-center">
              <span id="subtitle-preview" class="px-3 py-1 rounded-lg bg-gold-primary text-obsidian-bg font-bold text-xs shadow-lg">
                Capturing the golden essence of midnight Tokyo in 4K HDR ✨
              </span>
            </div>

            <!-- Timeline Scrubber -->
            <div class="absolute bottom-3 inset-x-3 flex items-center gap-2">
              <i data-lucide="play" class="w-4 h-4 text-gold-primary fill-gold-primary"></i>
              <input type="range" min="0" max="100" value="45" class="w-full accent-gold-primary h-1.5 bg-slate-700 rounded-lg">
              <span class="text-[10px] text-white font-mono">00:14/00:30</span>
            </div>
          </div>

          <!-- Studio Editing Tabs -->
          <div class="bg-obsidian-card border border-obsidian-border rounded-xl p-3 flex flex-col gap-3">
            <h4 class="text-xs font-bold text-gold-light uppercase tracking-wider">Cinematic Color LUTs</h4>
            <div class="grid grid-cols-4 gap-2">
              <button onclick="applyFilter('sepia(0.5) contrast(1.2) brightness(1.05)', 'Cinema Gold')" class="p-2 rounded-lg bg-gold-primary text-obsidian-bg text-xs font-bold">Cinema Gold</button>
              <button onclick="applyFilter('grayscale(1) contrast(1.4)', 'Noir 35mm')" class="p-2 rounded-lg bg-obsidian-elevated text-slate-200 text-xs font-semibold">Noir 35mm</button>
              <button onclick="applyFilter('hue-rotate(180deg) saturate(2)', 'Cyber Neon')" class="p-2 rounded-lg bg-obsidian-elevated text-slate-200 text-xs font-semibold">Cyber Neon</button>
              <button onclick="applyFilter('sepia(0.3) saturate(1.8)', 'Sunset Glow')" class="p-2 rounded-lg bg-obsidian-elevated text-slate-200 text-xs font-semibold">Sunset Glow</button>
            </div>

            <!-- Audio & Beat Sync -->
            <div class="pt-2 border-t border-obsidian-border flex items-center justify-between">
              <div class="flex items-center gap-2">
                <i data-lucide="music" class="w-4 h-4 text-gold-primary"></i>
                <span class="text-xs font-medium text-slate-200">Tokyo Neon Drift (Beat Synced ⚡)</span>
              </div>
              <span class="text-[10px] text-emerald-400 font-bold">128 BPM</span>
            </div>

            <!-- Subtitle Style -->
            <div class="pt-2 border-t border-obsidian-border flex items-center justify-between">
              <span class="text-xs font-medium text-slate-300">Subtitle Style:</span>
              <div class="flex gap-1.5">
                <button onclick="setSubStyle('gold')" class="px-2 py-0.5 rounded bg-gold-primary text-obsidian-bg text-[10px] font-bold">Karaoke Gold</button>
                <button onclick="setSubStyle('dark')" class="px-2 py-0.5 rounded bg-obsidian-elevated text-slate-200 text-[10px]">Pop Bold</button>
              </div>
            </div>
          </div>

          <!-- Publish Button -->
          <button onclick="publishFromStudio()" class="w-full py-3.5 rounded-xl bg-gold-primary hover:bg-gold-light text-obsidian-bg font-extrabold text-sm transition shadow-lg shadow-gold-primary/20 flex items-center justify-center gap-2">
            <i data-lucide="upload-cloud" class="w-4 h-4"></i>
            <span>Render 4K & Publish to Worldwide Feed</span>
          </button>
        </div>
      \`;
    }

    function applyFilter(filterCss, name) {
      const img = document.getElementById('stage-preview');
      const label = document.getElementById('active-lut-name');
      if (img) img.style.filter = filterCss;
      if (label) label.innerText = name;
    }

    function setSubStyle(style) {
      const sub = document.getElementById('subtitle-preview');
      if (sub) {
        if (style === 'gold') sub.className = 'px-3 py-1 rounded-lg bg-gold-primary text-obsidian-bg font-bold text-xs shadow-lg';
        else sub.className = 'px-3 py-1 rounded-lg bg-black/80 text-white border border-gold-primary font-bold text-xs shadow-lg';
      }
    }

    function publishFromStudio() {
      confetti({ particleCount: 80, spread: 70, origin: { y: 0.6 } });
      const newPost = {
        id: 'post_' + Date.now(),
        creator: {
          name: 'Aria Sterling',
          handle: '@ariasterling',
          avatar: '/assets/creator_avatar_elena_1790280911154.jpg',
          verified: true,
          isLive: false
        },
        caption: 'Midnight Reverie: Tokyo Neon Cut. Rendered with Cinema Gold LUT and multi-track beat sync audio.',
        tags: ['#CinemaGold', '#AURAWorldwide', '#TokyoNeo'],
        media: '/assets/sample_video_fashion_1790280956373.jpg',
        aspectRatio: '9:16',
        music: 'Tokyo Neon Drift · Synthwave (Original Audio)',
        likes: 1,
        fires: 1,
        trophies: 0,
        comments: 0,
        tipsUsd: 0,
        isTokenGated: false,
        userLiked: true,
        timeAgo: 'Just now'
      };
      state.posts.unshift(newPost);
      setTimeout(() => {
        switchTab('feed');
      }, 500);
    }

    // 3. WHATSAPP STYLE CHAT, AUDIO & VIDEO CALLS
    function renderChat(container) {
      container.innerHTML = \`
        <div class="flex flex-col h-full">
          <!-- Active Conversation Header -->
          <div class="p-3 bg-obsidian-surface border-b border-obsidian-border flex items-center justify-between">
            <div class="flex items-center gap-2.5">
              <img src="/assets/creator_avatar_elena_1790280911154.jpg" class="w-10 h-10 rounded-full object-cover ring-1 ring-gold-primary">
              <div>
                <div class="flex items-center gap-1">
                  <span class="text-sm font-bold text-slate-100">Elena Vance</span>
                  <i data-lucide="check-circle" class="w-3.5 h-3.5 text-gold-primary fill-gold-primary/20"></i>
                </div>
                <span class="text-[11px] text-emerald-400">Online · Tokyo Studio</span>
              </div>
            </div>

            <!-- Call Actions (WhatsApp style) -->
            <div class="flex items-center gap-1.5">
              <button onclick="openTipModal('Elena Vance', '/assets/creator_avatar_elena_1790280911154.jpg')" class="w-8 h-8 rounded-full bg-obsidian-card flex items-center justify-center text-gold-light hover:bg-gold-primary hover:text-obsidian-bg transition">
                <i data-lucide="zap" class="w-4 h-4"></i>
              </button>
              <button onclick="startCall('audio')" class="w-8 h-8 rounded-full bg-obsidian-card flex items-center justify-center text-slate-200 hover:text-gold-primary transition">
                <i data-lucide="phone" class="w-4 h-4"></i>
              </button>
              <button onclick="startCall('video')" class="w-8 h-8 rounded-full bg-obsidian-card flex items-center justify-center text-gold-primary hover:bg-gold-primary hover:text-obsidian-bg transition">
                <i data-lucide="video" class="w-4 h-4"></i>
              </button>
            </div>
          </div>

          <!-- Messages Stream -->
          <div class="flex-1 p-3 flex flex-col gap-3 overflow-y-auto">
            <!-- Encryption badge -->
            <div class="flex justify-center">
              <span class="text-[10px] text-slate-400 bg-obsidian-card px-3 py-1 rounded-full border border-obsidian-border text-center">
                🔒 End-to-end encrypted with Web3 P2P protocol.
              </span>
            </div>

            \${state.chatMessages.map(m => \`
              <div class="flex flex-col \${m.isMe ? 'items-end' : 'items-start'}">
                <div class="max-w-[80%] p-3 rounded-2xl \${m.isMe ? 'bg-obsidian-elevated border border-gold-muted text-slate-100 rounded-br-sm' : 'bg-obsidian-card border border-obsidian-border text-slate-200 rounded-bl-sm'}">
                  \${m.isVoice ? \`
                    <!-- Voice Note Player -->
                    <div class="flex items-center gap-3">
                      <button onclick="playVoiceNote()" class="w-8 h-8 rounded-full bg-gold-primary text-obsidian-bg flex items-center justify-center">
                        <i data-lucide="play" class="w-4 h-4 fill-obsidian-bg ml-0.5"></i>
                      </button>
                      <div class="flex gap-1 items-center">
                        \${[0.3, 0.6, 0.9, 0.4, 0.8, 1, 0.6, 0.3, 0.7, 0.5].map(h => \`
                          <div class="w-1 bg-gold-primary rounded-full" style="height: \${h * 20}px"></div>
                        \`).join('')}
                      </div>
                      <span class="text-[10px] font-mono text-slate-400">0:\${m.duration}</span>
                      <span class="text-[9px] font-bold px-1 rounded bg-obsidian-surface text-gold-light">1.5x</span>
                    </div>
                  \` : m.isTip ? \`
                    <!-- Tip Bubble -->
                    <div class="flex items-center gap-2 p-1.5 rounded-lg bg-gold-primary/10 border border-gold-muted mb-1">
                      <span class="text-xl">🪙</span>
                      <div>
                        <div class="text-xs font-bold text-gold-light">Direct Tip Sent</div>
                        <div class="text-[11px] text-emerald-400 font-mono">+\$\${m.tipAmount} USDC On-Chain</div>
                      </div>
                    </div>
                    <p class="text-xs">\${m.text}</p>
                  \` : \`
                    <p class="text-xs leading-relaxed">\${m.text}</p>
                  \`}
                  
                  <div class="mt-1 flex items-center justify-end gap-1 text-[9px] text-slate-400">
                    <span>\${m.time}</span>
                    \${m.isMe ? '<i data-lucide="check-check" class="w-3.5 h-3.5 text-gold-primary"></i>' : ''}
                  </div>
                </div>
              </div>
            \`).join('')}
          </div>

          <!-- Bottom Chat Input Bar with Interactive Voice Recording -->
          <div class="p-3 bg-obsidian-surface border-t border-obsidian-border">
            <div id="voice-recording-bar" class="hidden items-center justify-between p-2 rounded-2xl bg-red-950/40 border border-red-500/50 mb-2">
              <div class="flex items-center gap-2">
                <span class="w-2.5 h-2.5 rounded-full bg-red-500 animate-ping"></span>
                <span id="recording-timer-text" class="text-xs font-mono font-bold text-red-400">0:01</span>
              </div>
              <span class="text-[10px] text-slate-400">Recording voice message...</span>
              <button onclick="stopAndSendVoice()" class="px-3 py-1 rounded-full bg-gold-primary text-obsidian-bg font-bold text-xs">Send</button>
            </div>

            <div class="flex items-center gap-2">
              <input id="chat-input-text" type="text" placeholder="Type a message..." class="flex-1 bg-obsidian-card border border-obsidian-border rounded-full px-4 py-2 text-xs text-white focus:outline-none focus:border-gold-primary">
              
              <!-- Voice Note Button -->
              <button onclick="startVoiceRecording()" class="w-9 h-9 rounded-full bg-obsidian-elevated border border-gold-primary text-gold-light flex items-center justify-center hover:bg-gold-primary hover:text-obsidian-bg transition">
                <i data-lucide="mic" class="w-4 h-4"></i>
              </button>

              <button onclick="sendChatMessage()" class="w-9 h-9 rounded-full bg-gold-primary text-obsidian-bg flex items-center justify-center shadow-md">
                <i data-lucide="send" class="w-4 h-4 font-bold"></i>
              </button>
            </div>
          </div>
        </div>
      \`;
    }

    function sendChatMessage() {
      const input = document.getElementById('chat-input-text');
      if (input && input.value.trim()) {
        state.chatMessages.push({
          id: 'm_' + Date.now(),
          sender: 'Me',
          text: input.value.trim(),
          time: 'Just now',
          isMe: true
        });
        input.value = '';
        renderCurrentTab();
      }
    }

    function startVoiceRecording() {
      const bar = document.getElementById('voice-recording-bar');
      if (bar) bar.classList.remove('hidden');
      state.recordingSec = 0;
      state.recordingTimer = setInterval(() => {
        state.recordingSec++;
        const timerText = document.getElementById('recording-timer-text');
        if (timerText) timerText.innerText = '0:' + (state.recordingSec < 10 ? '0' + state.recordingSec : state.recordingSec);
      }, 1000);
    }

    function stopAndSendVoice() {
      clearInterval(state.recordingTimer);
      const bar = document.getElementById('voice-recording-bar');
      if (bar) bar.classList.add('hidden');
      state.chatMessages.push({
        id: 'vn_' + Date.now(),
        sender: 'Me',
        text: null,
        time: 'Just now',
        isMe: true,
        isVoice: true,
        duration: Math.max(state.recordingSec, 4)
      });
      renderCurrentTab();
    }

    // 4. PERSONALIZED CREATOR ANALYTICS RENDERER
    function renderAnalytics(container) {
      container.innerHTML = \`
        <div class="p-4 flex flex-col gap-4">
          <div class="flex items-center justify-between">
            <div>
              <h2 class="text-lg font-bold text-gold-light">Creator Intelligence</h2>
              <p class="text-xs text-slate-400">Worldwide Virality & Engagement Radar</p>
            </div>
            <span class="text-xs px-2.5 py-1 rounded-full bg-obsidian-elevated text-gold-primary border border-gold-muted font-bold">Last 30 Days</span>
          </div>

          <!-- KPI Cards Grid -->
          <div class="grid grid-cols-2 gap-3">
            <div class="p-3 bg-obsidian-card border border-obsidian-border rounded-xl">
              <span class="text-xs text-slate-400">Worldwide Reach</span>
              <div class="text-xl font-black text-slate-100 font-mono mt-1">4.82M</div>
              <span class="text-[10px] text-emerald-400 font-bold">+28.4% this week</span>
            </div>
            <div class="p-3 bg-obsidian-card border border-obsidian-border rounded-xl">
              <span class="text-xs text-slate-400">Video Views</span>
              <div class="text-xl font-black text-slate-100 font-mono mt-1">1.24M</div>
              <span class="text-[10px] text-emerald-400 font-bold">+34.1% this week</span>
            </div>
            <div class="p-3 bg-obsidian-card border border-obsidian-border rounded-xl">
              <span class="text-xs text-slate-400">Watch Time</span>
              <div class="text-xl font-black text-slate-100 font-mono mt-1">186.4K hrs</div>
              <span class="text-[10px] text-emerald-400 font-bold">+18.2% this week</span>
            </div>
            <div class="p-3 bg-obsidian-card border border-obsidian-border rounded-xl">
              <span class="text-xs text-slate-400">On-Chain Revenue</span>
              <div class="text-xl font-black text-gold-light font-mono mt-1">\$18,450</div>
              <span class="text-[10px] text-emerald-400 font-bold">+42.5% this week</span>
            </div>
          </div>

          <!-- Virality Index -->
          <div class="p-4 bg-obsidian-card border border-gold-primary rounded-xl flex items-center justify-between shadow-lg">
            <div>
              <div class="flex items-center gap-1.5">
                <span class="text-sm font-bold text-gold-light">Virality Index Score</span>
                <span class="text-[9px] font-bold px-1.5 py-0.5 rounded bg-emerald-500/20 text-emerald-400">APEX TIER</span>
              </div>
              <p class="text-xs text-slate-300 mt-1">Top 0.8% of global creators on AURA network</p>
            </div>
            <div class="w-14 h-14 rounded-full bg-gold-primary text-obsidian-bg flex items-center justify-center font-black text-lg shadow-md font-mono">
              96.4
            </div>
          </div>

          <!-- Worldwide Hotspots -->
          <div class="p-4 bg-obsidian-card border border-obsidian-border rounded-xl flex flex-col gap-3">
            <h4 class="text-xs font-bold text-slate-200 uppercase tracking-wider">Worldwide Geographic Hotspots</h4>
            \${[
              { country: 'Tokyo, Japan', flag: '🇯🇵', percent: 32, views: '396.8K views' },
              { country: 'New York, USA', flag: '🇺🇸', percent: 26, views: '322.4K views' },
              { country: 'London, UK', flag: '🇬🇧', percent: 18, views: '223.2K views' },
              { country: 'Dubai, UAE', flag: '🇦🇪', percent: 12, views: '148.8K views' }
            ].map(h => \`
              <div>
                <div class="flex justify-between text-xs mb-1">
                  <span>\${h.flag} \${h.country}</span>
                  <span class="font-bold text-gold-light font-mono">\${h.percent}% (\${h.views})</span>
                </div>
                <div class="w-full h-1.5 bg-obsidian-elevated rounded-full overflow-hidden">
                  <div class="h-full bg-gold-primary rounded-full" style="width: \${h.percent}%"></div>
                </div>
              </div>
            \`).join('')}
          </div>
        </div>
      \`;
    }

    // 5. BLOCKCHAIN VAULT & MONETIZATION RENDERER
    function renderVault(container) {
      container.innerHTML = \`
        <div class="p-4 flex flex-col gap-4">
          <!-- Multi-Chain Smart Wallet -->
          <div class="p-4 bg-obsidian-card border-2 border-gold-primary rounded-2xl shadow-xl">
            <div class="flex items-center justify-between text-xs">
              <div class="flex items-center gap-1.5">
                <span class="w-2 h-2 rounded-full bg-emerald-400"></span>
                <span class="font-bold text-gold-light">Web3 Multi-Chain Vault</span>
              </div>
              <span class="text-slate-400 font-mono text-[11px]">\${state.wallet.ethAddress}</span>
            </div>

            <div class="mt-3">
              <span class="text-xs text-slate-400">Total Portfolio Value</span>
              <div class="text-2xl font-black text-white font-mono mt-0.5">\$34,280.00 USDC</div>
            </div>

            <!-- Balances -->
            <div class="grid grid-cols-3 gap-2 mt-4">
              <div class="p-2.5 rounded-xl bg-obsidian-elevated border border-obsidian-border text-center">
                <span class="text-[10px] text-gold-primary font-bold">\$AURA</span>
                <div class="text-xs font-black font-mono mt-1 text-slate-100">14,250</div>
              </div>
              <div class="p-2.5 rounded-xl bg-obsidian-elevated border border-obsidian-border text-center">
                <span class="text-[10px] text-purple-400 font-bold">ETH</span>
                <div class="text-xs font-black font-mono mt-1 text-slate-100">\${state.wallet.ethBalance}</div>
              </div>
              <div class="p-2.5 rounded-xl bg-obsidian-elevated border border-obsidian-border text-center">
                <span class="text-[10px] text-blue-400 font-bold">USDC</span>
                <div class="text-xs font-black font-mono mt-1 text-slate-100">\${state.wallet.usdcBalance}</div>
              </div>
            </div>

            <!-- Download APK Button -->
            <div class="mt-4 pt-4 border-t border-obsidian-border flex gap-2">
              <a href="/download-apk" class="flex-1 py-2.5 rounded-xl bg-gold-primary hover:bg-gold-light text-obsidian-bg font-bold text-xs text-center transition flex items-center justify-center gap-1.5">
                <i data-lucide="download" class="w-4 h-4"></i>
                <span>Download Android APK</span>
              </a>
            </div>
          </div>

          <!-- Exclusive Content Subscriptions -->
          <div class="flex flex-col gap-3">
            <h3 class="text-sm font-bold text-gold-light uppercase tracking-wider">Exclusive Content Subscriptions</h3>
            
            \${[
              { name: 'Gold Inner Circle', price: 19, active: true, subs: 482, desc: 'Unlock all Token-Gated 4K Videos, raw DaVinci node trees & VIP chat priority.' },
              { name: "Director's Cut Pass", price: 49, active: false, subs: 194, desc: 'Complete project source files, uncompressed ProRes 4444 footage & commercial licenses.' }
            ].map(t => \`
              <div class="p-4 bg-obsidian-card border border-obsidian-border rounded-xl flex flex-col gap-2">
                <div class="flex items-center justify-between">
                  <div class="flex items-center gap-2">
                    <span class="text-sm font-bold text-slate-100">\${t.name}</span>
                    \${t.active ? '<span class="text-[9px] px-1.5 py-0.5 rounded bg-gold-primary text-obsidian-bg font-extrabold uppercase">ACTIVE</span>' : ''}
                  </div>
                  <span class="text-base font-black text-gold-light font-mono">\$\${t.price}/mo</span>
                </div>
                <p class="text-xs text-slate-300">\${t.desc}</p>
                <button onclick="mintPass('\${t.name}', \${t.price})" class="mt-2 py-2 rounded-lg \${t.active ? 'bg-obsidian-elevated border border-gold-muted text-gold-light' : 'bg-gold-primary text-obsidian-bg font-bold'} text-xs transition">
                  \${t.active ? 'Manage NFT Pass' : 'Mint Pass with USDC'}
                </button>
              </div>
            \`).join('')}
          </div>
        </div>
      \`;
    }

    function mintPass(name, price) {
      confetti({ particleCount: 70, spread: 60, origin: { y: 0.7 } });
      alert('👑 Successfully minted on-chain pass: ' + name + ' for $' + price + ' USDC!');
    }

    // Modal Helpers: Tip, Search, Notifications, Stories, Call
    function openTipModal(name, avatar) {
      const modal = document.getElementById('modal-container');
      modal.innerHTML = \`
        <div class="fixed inset-0 z-50 bg-black/80 backdrop-blur-sm flex items-center justify-center p-4">
          <div class="w-full max-w-sm bg-obsidian-card border-2 border-gold-primary rounded-2xl p-5 shadow-2xl flex flex-col gap-4">
            <div class="flex items-center justify-between">
              <h3 class="text-base font-bold text-gold-light">Direct Creator Tip</h3>
              <button onclick="closeModal()" class="text-slate-400 hover:text-white">✕</button>
            </div>
            <div class="flex items-center gap-3 p-3 rounded-xl bg-obsidian-elevated">
              <img src="\${avatar}" class="w-12 h-12 rounded-full object-cover ring-1 ring-gold-primary">
              <div>
                <span class="text-sm font-bold text-white">\${name}</span>
                <p class="text-xs text-slate-400">Web3 Smart Wallet Verified</p>
              </div>
            </div>
            <div class="grid grid-cols-4 gap-2">
              \${[10, 25, 50, 100].map(amt => \`
                <button onclick="sendTipAmount(\${amt}, '\${name}')" class="py-2.5 rounded-xl bg-obsidian-elevated hover:bg-gold-primary hover:text-obsidian-bg border border-obsidian-border text-xs font-bold text-gold-light transition font-mono">
                  \$\${amt}
                </button>
              \`).join('')}
            </div>
          </div>
        </div>
      \`;
    }

    function sendTipAmount(amt, name) {
      confetti({ particleCount: 100, spread: 80, origin: { y: 0.6 } });
      closeModal();
      alert('✨ On-chain tip of $' + amt + ' USDC sent directly to ' + name + '!');
    }

    function startCall(type) {
      const modal = document.getElementById('modal-container');
      modal.innerHTML = \`
        <div class="fixed inset-0 z-50 bg-black flex flex-col justify-between p-6">
          <div class="flex justify-between items-center text-white">
            <div>
              <h3 class="text-base font-bold text-white">Elena Vance</h3>
              <span class="text-xs text-gold-light font-mono">00:42 · 4K HDR</span>
            </div>
            <button onclick="closeModal()" class="w-10 h-10 rounded-full bg-red-600 text-white flex items-center justify-center font-bold">✕</button>
          </div>

          <div class="flex-1 flex items-center justify-center">
            \${type === 'video' ? \`
              <img src="/assets/sample_video_fashion_1790280956373.jpg" class="max-h-[60vh] rounded-2xl object-cover ring-2 ring-gold-primary filter brightness-105">
            \` : \`
              <div class="w-32 h-32 rounded-full ring-4 ring-gold-primary bg-obsidian-elevated flex items-center justify-center animate-pulse">
                <img src="/assets/creator_avatar_elena_1790280911154.jpg" class="w-28 h-28 rounded-full object-cover">
              </div>
            \`}
          </div>

          <div class="flex justify-center gap-4">
            <button class="w-12 h-12 rounded-full bg-slate-800 text-white flex items-center justify-center">
              <i data-lucide="mic" class="w-5 h-5"></i>
            </button>
            <button onclick="closeModal()" class="w-14 h-14 rounded-full bg-red-600 text-white flex items-center justify-center shadow-lg">
              <i data-lucide="phone-off" class="w-6 h-6"></i>
            </button>
          </div>
        </div>
      \`;
      lucide.createIcons();
    }

    function openStoryViewer(idx) {
      const s = state.stories[idx];
      const modal = document.getElementById('modal-container');
      modal.innerHTML = \`
        <div class="fixed inset-0 z-50 bg-black flex flex-col justify-between p-4">
          <div class="w-full h-1 bg-slate-700 rounded-full overflow-hidden mb-3">
            <div class="w-2/3 h-full bg-gold-primary"></div>
          </div>
          <div class="flex items-center justify-between text-white">
            <div class="flex items-center gap-2">
              <img src="\${s.avatar}" class="w-9 h-9 rounded-full object-cover ring-1 ring-gold-primary">
              <span class="text-xs font-bold">\${s.name}</span>
            </div>
            <button onclick="closeModal()" class="text-white text-lg">✕</button>
          </div>
          <div class="flex-1 flex items-center justify-center my-4 overflow-hidden rounded-2xl">
            <img src="\${s.media}" class="w-full h-full object-cover rounded-2xl">
          </div>
          <p class="text-xs text-white text-center mb-2">\${s.caption}</p>
        </div>
      \`;
    }

    function openSearchModal() {
      const modal = document.getElementById('modal-container');
      modal.innerHTML = \`
        <div class="fixed inset-0 z-50 bg-black/80 backdrop-blur-sm flex items-center justify-center p-4">
          <div class="w-full max-w-sm bg-obsidian-card border border-gold-primary rounded-2xl p-4 flex flex-col gap-3">
            <div class="flex justify-between items-center">
              <h3 class="text-sm font-bold text-gold-light">Worldwide Search</h3>
              <button onclick="closeModal()" class="text-slate-400">✕</button>
            </div>
            <input type="text" placeholder="Search creators, tags, sounds..." class="w-full bg-obsidian-elevated border border-obsidian-border rounded-xl px-3 py-2 text-xs text-white focus:outline-none focus:border-gold-primary">
            <div class="flex flex-col gap-1.5 mt-2">
              <span class="text-[10px] text-slate-400 font-bold uppercase">Trending</span>
              <span class="text-xs text-slate-200">#CinemaGold</span>
              <span class="text-xs text-slate-200">Elena Vance Tokyo 4K</span>
              <span class="text-xs text-slate-200">Web3 Creator Grants</span>
            </div>
          </div>
        </div>
      \`;
    }

    function openNotificationsModal() {
      const modal = document.getElementById('modal-container');
      modal.innerHTML = \`
        <div class="fixed inset-0 z-50 bg-black/80 backdrop-blur-sm flex items-center justify-center p-4">
          <div class="w-full max-w-sm bg-obsidian-card border border-obsidian-border rounded-2xl p-4 flex flex-col gap-3">
            <div class="flex justify-between items-center">
              <h3 class="text-sm font-bold text-gold-light">AURA Notifications</h3>
              <button onclick="closeModal()" class="text-slate-400">✕</button>
            </div>
            <div class="flex flex-col gap-2">
              <div class="p-2.5 rounded-lg bg-obsidian-elevated text-xs text-slate-200">🪙 Elena Vance sent you a 50 USDC tip on Tokyo Runway post</div>
              <div class="p-2.5 rounded-lg bg-obsidian-elevated text-xs text-slate-200">👑 New VIP Member joined your Gold Inner Circle pass (+19 USDC)</div>
              <div class="p-2.5 rounded-lg bg-obsidian-elevated text-xs text-slate-200">🔥 Video "Tokyo Night Reverie" is surging on Worldwide Viral</div>
            </div>
          </div>
        </div>
      \`;
    }

    function closeModal() {
      document.getElementById('modal-container').innerHTML = '';
    }

    // Init
    renderCurrentTab();
  </script>
</body>
</html>`;
}
