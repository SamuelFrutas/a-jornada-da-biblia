const chapters = [
  { id: 1, title: 'A Criação', ref: 'Gênesis 1', summary: 'Deus cria os céus, a terra e tudo o que nela existe. A luz surge e a criação é organizada.', xp: 50, unlock: null, scene: 'criacao', discoveries: [['A criação', 'Gênesis 1', 'O capítulo apresenta Deus como Criador e organiza a criação em seis dias, culminando na criação do ser humano.']], question: { q: 'Segundo Gênesis 1:3, o que Deus disse para surgir?', options: ['A luz', 'A chuva', 'Jerusalém', 'O templo'], correct: 0, explain: 'Deus disse: “Haja luz”, e houve luz.', ref: 'Gênesis 1:3' } },
  { id: 2, title: 'O Jardim do Éden', ref: 'Gênesis 2', summary: 'O ser humano é colocado no jardim, recebe uma missão e conhece o mandamento de Deus.', xp: 60, unlock: 1, scene: 'eden', discoveries: [['Éden', 'Gênesis 2', 'O Éden é o jardim onde Deus colocou o ser humano. O relato também apresenta o rio que saía do jardim e se dividia em quatro braços.'], ['Adão', 'Gênesis 2', 'O homem é formado do pó da terra, recebe vida e é colocado no jardim para cultivá-lo e guardá-lo.']], question: { q: 'Qual era o nome do jardim onde Deus colocou o homem?', options: ['Jardim do Éden', 'Jardim do Sinai', 'Jardim de Belém', 'Jardim de Jerusalém'], correct: 0, explain: 'Gênesis 2:8 diz que Deus plantou um jardim no Éden e ali colocou o homem.', ref: 'Gênesis 2:8' } },
  { id: 3, title: 'A Queda', ref: 'Gênesis 3', summary: 'A serpente engana a mulher, o casal desobedece e a narrativa apresenta as consequências do pecado.', xp: 70, unlock: 2, scene: 'queda', discoveries: [['A serpente', 'Gênesis 3', 'A serpente aparece no relato conversando com a mulher e distorcendo o mandamento de Deus.'], ['A queda', 'Gênesis 3', 'A desobediência traz consequências e termina com a saída do homem e da mulher do jardim.']], question: { q: 'O que aconteceu depois que o homem e a mulher pecaram?', options: ['Foram expulsos do jardim', 'Foram levados ao Egito', 'Construíram uma cidade', 'Receberam uma coroa'], correct: 0, explain: 'Deus os expulsou do jardim do Éden, e querubins passaram a guardar o caminho da árvore da vida.', ref: 'Gênesis 3:23–24' } }
];

const achievements = [
  ['no-principio', 'No princípio', 'Conclua Gênesis 1.'], ['o-jardim', 'O Jardim', 'Conclua Gênesis 2.'], ['a-queda', 'A Queda', 'Conclua Gênesis 3.'], ['primeiro-passo', 'Primeiro passo', 'Complete sua primeira fase.'], ['pequena-jornada', 'Pequena jornada', 'Conclua os três capítulos do protótipo.']
];

const STORAGE_KEY = 'jornadaBiblia';
const defaultState = () => ({ xp: 0, completed: [], discoveries: [], achievements: [] });
let state = defaultState();
let current = null;
let initialized = false;
const $ = (selector) => document.querySelector(selector);

function cloneDefault() { return defaultState(); }

function normalizeState(value) {
  const base = defaultState();
  if (!value || typeof value !== 'object') return base;
  return {
    xp: Number.isFinite(Number(value.xp)) ? Number(value.xp) : 0,
    completed: Array.isArray(value.completed) ? value.completed.map(Number).filter(Number.isFinite) : [],
    discoveries: Array.isArray(value.discoveries) ? value.discoveries : [],
    achievements: Array.isArray(value.achievements) ? value.achievements : []
  };
}

function load() {
  try {
    const saved = localStorage.getItem(STORAGE_KEY);
    return saved ? normalizeState(JSON.parse(saved)) : cloneDefault();
  } catch (error) {
    console.warn('Não foi possível carregar o progresso.', error);
    return cloneDefault();
  }
}

function save() {
  try { localStorage.setItem(STORAGE_KEY, JSON.stringify(state)); }
  catch (error) { console.warn('Não foi possível salvar o progresso.', error); }
}

function show(id) {
  document.querySelectorAll('.screen').forEach((screen) => screen.classList.toggle('active', screen.id === id));
  window.scrollTo(0, 0);
}

function toast(text) {
  const element = $('#toast');
  if (!element) return;
  element.textContent = text;
  element.classList.add('show');
  clearTimeout(toast.timer);
  toast.timer = setTimeout(() => element.classList.remove('show'), 2200);
}

function renderHeader() {
  const levelText = $('#levelText'), xpText = $('#xpText'), xpFill = $('#xpFill'), continueBtn = $('#continueBtn');
  if (!levelText || !xpText || !xpFill || !continueBtn) return;
  const level = Math.floor(state.xp / 100) + 1;
  const inLevel = state.xp % 100;
  levelText.textContent = `Nível ${level}`;
  xpText.textContent = `${state.xp} XP`;
  xpFill.style.width = `${Math.min(inLevel, 100)}%`;
  continueBtn.hidden = state.completed.length === 0;
}

function renderMap() {
  const progress = $('#mapProgress'), list = $('#chapterNodes');
  if (!progress || !list) return;
  renderHeader();
  progress.textContent = `${Math.round((state.completed.length / chapters.length) * 100)}%`;
  list.innerHTML = chapters.map((chapter) => {
    const complete = state.completed.includes(chapter.id);
    const unlocked = chapter.unlock === null || state.completed.includes(chapter.unlock);
    return `<article class="chapter-card ${unlocked ? '' : 'locked'} ${complete ? 'done' : ''}">
      <button class="node ${complete ? 'done-node' : ''}" type="button" data-chapter="${unlocked ? chapter.id : ''}" aria-label="${unlocked ? `Abrir ${chapter.title}` : `Bloqueado: ${chapter.title}`}" ${unlocked ? '' : 'disabled'}>${complete ? '✓' : chapter.id}</button>
      <div class="chapter-copy"><span>${chapter.ref}</span><h3>${chapter.title}</h3><p>${chapter.summary}</p></div>
      ${unlocked ? `<button class="enter" type="button" data-chapter="${chapter.id}">${complete ? 'REVISITAR' : 'ENTRAR'} <b>›</b></button>` : '<span class="lock">○</span>'}
    </article>`;
  }).join('');
}

function openChapter(id) {
  const chapter = chapters.find((item) => item.id === id);
  if (!chapter) return;
  current = chapter;
  show('chapter');
  const label = $('#chapterLabel'), progress = $('#chapterProgress'), xp = $('#chapterXp'), body = $('#chapterBody');
  if (!label || !progress || !xp || !body) return;
  label.textContent = 'GÊNESIS';
  progress.textContent = `Capítulo ${chapter.id} de ${chapters.length}`;
  xp.textContent = `+${chapter.xp} XP`;
  body.innerHTML = `<div class="scene scene-${chapter.scene}"><div class="scene-sun"></div><div class="scene-hills"></div><div class="scene-title"><span>${chapter.ref}</span><h1>${chapter.title}</h1></div></div>
    <article class="story-card"><div class="story-intro"><span class="chapter-number">JORNADA ${String(chapter.id).padStart(2, '0')}</span><p>${chapter.summary}</p><span class="reference">📖 ${chapter.ref}</span></div>
    <div class="lesson"><div class="lesson-icon">✦</div><div><strong>Observe o texto</strong><p>Leia o capítulo na sua Bíblia e depois enfrente o desafio. O jogo foi feito para acompanhar a leitura, não para substituí-la.</p></div></div>
    <div class="question"><span class="question-label">DESAFIO DA JORNADA</span><h2>${chapter.question.q}</h2><div class="answers">${chapter.question.options.map((option, index) => `<button class="answer" type="button" data-answer="${index}"><span>${String.fromCharCode(65 + index)}</span>${option}</button>`).join('')}</div><div id="feedback"></div></div></article>`;
}

function answer(choice) {
  if (!current) return;
  const question = current.question;
  const buttons = document.querySelectorAll('[data-answer]');
  buttons.forEach((button) => { button.disabled = true; });
  if (buttons[question.correct]) buttons[question.correct].classList.add('correct');
  if (choice !== question.correct && buttons[choice]) buttons[choice].classList.add('wrong');
  const correct = choice === question.correct;
  const feedback = $('#feedback');
  if (!feedback) return;
  feedback.innerHTML = `<div class="feedback ${correct ? 'good' : 'retry'}"><strong>${correct ? '✓ Muito bem!' : 'Quase! Vamos aprender.'}</strong><p>${question.explain}</p><span class="reference">📖 ${question.ref}</span><button class="next" id="finishBtn" type="button">${current.id === 3 ? 'CONCLUIR JORNADA' : 'CONTINUAR JORNADA'} <b>→</b></button></div>`;
  if (correct) toast('Resposta correta · +XP ao concluir');
}

function unlockAchievement(id) {
  if (state.achievements.includes(id)) return;
  state.achievements.push(id);
  const achievement = achievements.find((item) => item[0] === id);
  if (achievement) toast(`Conquista: ${achievement[1]}`);
}

function finishChapter() {
  if (!current) return;
  if (!state.completed.includes(current.id)) {
    state.completed.push(current.id);
    state.xp += current.xp;
    current.discoveries.forEach((discovery) => {
      if (!state.discoveries.some((item) => item[0] === discovery[0])) state.discoveries.push(discovery);
    });
    unlockAchievement(current.id === 1 ? 'no-principio' : current.id === 2 ? 'o-jardim' : 'a-queda');
    if (state.completed.length === 1) unlockAchievement('primeiro-passo');
    if (state.completed.length === chapters.length) unlockAchievement('pequena-jornada');
    save();
  }
  renderMap();
  show('map');
}

function renderLibrary() {
  const count = $('#libraryCount'), body = $('#libraryBody');
  if (!count || !body) return;
  renderHeader();
  count.textContent = String(state.discoveries.length);
  body.innerHTML = state.discoveries.length ? `<div class="library-intro"><span>COLEÇÃO</span><h2>O que você descobriu</h2><p>Novas páginas serão desbloqueadas conforme sua jornada avança.</p></div><div class="library-grid">${state.discoveries.map((item, index) => `<article class="library-card"><div class="card-icon">${index % 3 === 0 ? '✦' : index % 3 === 1 ? '⌖' : '◈'}</div><small>${item[1]}</small><h3>${item[0]}</h3><p>${item[2]}</p></article>`).join('')}</div>` : '<div class="empty"><div>📖</div><h2>A biblioteca está esperando você.</h2><p>Complete Gênesis 1 para desbloquear sua primeira descoberta.</p></div>';
  show('library');
}

function renderAchievements() {
  const body = $('#achievementBody');
  if (!body) return;
  body.innerHTML = `<div class="library-intro"><span>MARCOS</span><h2>Sua jornada</h2><p>${state.achievements.length} de ${achievements.length} conquistas desbloqueadas.</p></div><div class="achievement-grid">${achievements.map((item) => { const done = state.achievements.includes(item[0]); return `<article class="achievement ${done ? 'unlocked' : ''}"><div class="badge">${done ? '★' : '☆'}</div><div><h3>${item[1]}</h3><p>${item[2]}</p></div></article>`; }).join('')}</div>`;
  show('achievements');
}

function resetProgress() {
  if (!window.confirm('Reiniciar todo o progresso desta jornada?')) return;
  state = cloneDefault();
  current = null;
  save();
  renderMap();
  show('map');
  toast('Jornada reiniciada');
}

function handleAction(action) {
  if (action === 'map') { renderMap(); show('map'); }
  else if (action === 'home') show('home');
  else if (action === 'library') renderLibrary();
  else if (action === 'achievements') renderAchievements();
  else if (action === 'reset') resetProgress();
}

function bindEvents() {
  document.addEventListener('click', (event) => {
    const chapterButton = event.target.closest('[data-chapter]');
    if (chapterButton && chapterButton.dataset.chapter && !chapterButton.disabled) { event.preventDefault(); openChapter(Number(chapterButton.dataset.chapter)); return; }
    const answerButton = event.target.closest('[data-answer]');
    if (answerButton && !answerButton.disabled) { event.preventDefault(); answer(Number(answerButton.dataset.answer)); return; }
    const finishButton = event.target.closest('#finishBtn');
    if (finishButton) { event.preventDefault(); finishChapter(); return; }
    const actionButton = event.target.closest('[data-action]');
    if (actionButton) { event.preventDefault(); handleAction(actionButton.dataset.action); }
  });
  const startButton = $('#startBtn'), continueButton = $('#continueBtn');
  if (startButton) startButton.addEventListener('click', (event) => { event.preventDefault(); state = cloneDefault(); current = null; save(); renderMap(); show('map'); });
  if (continueButton) continueButton.addEventListener('click', (event) => { event.preventDefault(); renderMap(); show('map'); });
}

function init() {
  if (initialized) return;
  initialized = true;
  state = load();
  bindEvents();
  renderHeader();
}

if (document.readyState === 'loading') document.addEventListener('DOMContentLoaded', init, { once: true });
else init();
