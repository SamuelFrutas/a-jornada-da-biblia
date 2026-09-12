/* Inicialização robusta para celular e para versões antigas em cache. */
(function () {
  function callGame(name, arg) {
    try {
      if (typeof window[name] === 'function') return arg === undefined ? window[name]() : window[name](arg);
      if (typeof window.eval === 'function') {
        return arg === undefined ? window.eval(name + '()') : window.eval(name + '(' + Number(arg) + ')');
      }
    } catch (error) {
      console.error('Falha ao chamar ' + name + ':', error);
    }
    return false;
  }

  function showMapFallback() {
    var home = document.getElementById('home');
    var map = document.getElementById('map');
    var list = document.getElementById('chapterNodes');
    if (home) home.classList.remove('active');
    if (map) map.classList.add('active');
    if (list && !list.innerHTML.trim()) {
      list.innerHTML = '<article class="chapter-card" style="left:6%;top:18%"><button class="node" type="button" data-fallback-chapter="1">1</button><div class="chapter-copy"><span>GÊNESIS 1</span><h3>A Criação</h3><p>Entre no princípio e explore a criação.</p></div><button class="enter" type="button" data-fallback-chapter="1">JOGAR ›</button></article>' +
        '<article class="chapter-card locked" style="left:31%;top:42%"><button class="node" type="button" disabled>2</button><div class="chapter-copy"><span>GÊNESIS 2</span><h3>O Jardim do Éden</h3><p>Complete o capítulo anterior para avançar.</p></div><span class="lock">○</span></article>' +
        '<article class="chapter-card locked" style="right:5%;top:62%"><button class="node" type="button" disabled>3</button><div class="chapter-copy"><span>GÊNESIS 3</span><h3>A Queda</h3><p>Complete a jornada para avançar.</p></div><span class="lock">○</span></article>';
    }
  }

  function startJourney() {
    try {
      var ok = callGame('renderMap');
      if (!ok) showMapFallback();
    } catch (error) {
      console.error('Falha ao iniciar a jornada:', error);
      showMapFallback();
    }
  }

  function startChapter(id) {
    try {
      var ok = callGame('openChapter', id);
      if (!ok) {
        var home = document.getElementById('home');
        var map = document.getElementById('map');
        var chapter = document.getElementById('chapter');
        var body = document.getElementById('chapterBody');
        if (home) home.classList.remove('active');
        if (map) map.classList.remove('active');
        if (chapter) chapter.classList.add('active');
        if (body) body.innerHTML = '<div class="game-shell"><div class="narration"><div class="narration-tag">GÊNESIS 1</div><h1>A Criação</h1><p>Entre no princípio. Explore a cena e descubra, passo a passo, o relato da criação.</p><div class="reference">📖 Gênesis 1:1–31</div><p><b>O jogo está carregando.</b> Toque em “Voltar ao mapa” e tente novamente.</p></div></div>';
      }
    } catch (error) {
      console.error('Falha ao abrir capítulo:', error);
    }
  }

  window.startJourney = startJourney;
  window.startChapter = startChapter;

  function bindFallback() {
    var button = document.getElementById('startBtn');
    if (button && button.dataset.fallbackBound !== '1') {
      button.dataset.fallbackBound = '1';
      button.addEventListener('click', startJourney);
    }
    document.addEventListener('click', function (event) {
      var chapterButton = event.target.closest('[data-chapter]');
      if (chapterButton && chapterButton.dataset.chapter) {
        startChapter(Number(chapterButton.dataset.chapter));
      }
      var fallbackButton = event.target.closest('[data-fallback-chapter]');
      if (fallbackButton && fallbackButton.dataset.fallbackChapter) {
        startChapter(Number(fallbackButton.dataset.fallbackChapter));
      }
    }, true);
  }

  if (document.readyState === 'loading') {
    document.addEventListener('DOMContentLoaded', bindFallback, { once: true });
  } else {
    bindFallback();
  }
})();
