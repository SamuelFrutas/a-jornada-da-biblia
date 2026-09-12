/* Fallback de inicialização para dispositivos móveis e cache antigo. */
(function () {
  function startJourney() {
    try {
      if (typeof window.renderMap === 'function') {
        if (typeof window.defaultState === 'function') window.state = window.defaultState();
        if (typeof window.save === 'function') window.save();
        window.renderMap();
        return;
      }
      var home = document.getElementById('home');
      var map = document.getElementById('map');
      if (home && map) {
        home.classList.remove('active');
        map.classList.add('active');
      }
    } catch (error) {
      console.error('Falha ao iniciar a jornada:', error);
      var homeEl = document.getElementById('home');
      var mapEl = document.getElementById('map');
      if (homeEl && mapEl) {
        homeEl.classList.remove('active');
        mapEl.classList.add('active');
      }
    }
  }

  window.startJourney = startJourney;

  function bindFallback() {
    var button = document.getElementById('startBtn');
    if (!button || button.dataset.fallbackBound === '1') return;
    button.dataset.fallbackBound = '1';
    button.addEventListener('pointerup', startJourney, { passive: true });
    button.addEventListener('click', startJourney);
  }

  if (document.readyState === 'loading') {
    document.addEventListener('DOMContentLoaded', bindFallback, { once: true });
  } else {
    bindFallback();
  }
})();
