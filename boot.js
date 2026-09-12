/* Inicialização robusta para celular e para versões antigas em cache. */
(function () {
  function showMapFallback() {
    var home = document.getElementById('home');
    var map = document.getElementById('map');
    var list = document.getElementById('chapterNodes');
    if (home) home.classList.remove('active');
    if (map) map.classList.add('active');
    if (list && !list.innerHTML.trim()) {
      list.innerHTML = '<article class="chapter-card" style="left:6%;top:18%"><button class="node" type="button" onclick="if(window.openChapter){window.openChapter(1)}">1</button><div class="chapter-copy"><span>GÊNESIS 1</span><h3>A Criação</h3><p>Entre no princípio e explore a criação.</p></div><button class="enter" type="button" onclick="if(window.openChapter){window.openChapter(1)}">JOGAR ›</button></article>' +
        '<article class="chapter-card locked" style="left:31%;top:42%"><button class="node" type="button" disabled>2</button><div class="chapter-copy"><span>GÊNESIS 2</span><h3>O Jardim do Éden</h3><p>Complete o capítulo anterior para avançar.</p></div><span class="lock">○</span></article>' +
        '<article class="chapter-card locked" style="right:5%;top:62%"><button class="node" type="button" disabled>3</button><div class="chapter-copy"><span>GÊNESIS 3</span><h3>A Queda</h3><p>Complete a jornada para avançar.</p></div><span class="lock">○</span></article>';
    }
  }

  function startJourney() {
    try {
      if (typeof window.renderMap === 'function') window.renderMap();
      else showMapFallback();
    } catch (error) {
      console.error('Falha ao iniciar a jornada:', error);
      showMapFallback();
    }
  }

  window.startJourney = startJourney;

  function bindFallback() {
    var button = document.getElementById('startBtn');
    if (!button || button.dataset.fallbackBound === '1') return;
    button.dataset.fallbackBound = '1';
    button.addEventListener('click', startJourney);
  }

  if (document.readyState === 'loading') {
    document.addEventListener('DOMContentLoaded', bindFallback, { once: true });
  } else {
    bindFallback();
  }
})();
