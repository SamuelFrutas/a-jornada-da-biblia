const TOTAL_ETAPAS = 1189;
const ESTADO_KEY = 'jornada-biblia-estado-v1';
const CONCLUIDAS_KEY = 'jornada-biblia-concluidas-v1';

function getState(){try{return JSON.parse(localStorage.getItem(ESTADO_KEY))||{index:0}}catch{return{index:0}}}
function getCompleted(){try{return JSON.parse(localStorage.getItem(CONCLUIDAS_KEY))||[]}catch{return[]}}
function saveCompleted(list){localStorage.setItem(CONCLUIDAS_KEY,JSON.stringify(list))}

const els={title:document.getElementById('readingTitle'),range:document.getElementById('readingRange'),reason:document.getElementById('readingReason'),link:document.getElementById('bibleLink'),devotional:document.getElementById('devotional'),percent:document.getElementById('progressPercent'),bar:document.getElementById('progressBar'),progressText:document.getElementById('progressText'),complete:document.getElementById('completeBtn'),readAloud:document.getElementById('readAloudBtn'),home:document.getElementById('homeView'),history:document.getElementById('historyView'),historyList:document.getElementById('historyList')};

function render(){
 const state=getState(); const item=LEITURAS[state.index]||LEITURAS[0]; const completed=getCompleted();
 els.title.textContent=item.title; els.range.textContent=item.range; els.reason.textContent=item.reason; els.link.href=item.sbb;
 const labels=[['🧠 1. Explicação do texto',item.devotional.explicacao],['🏺 2. Contexto — tempo e cultura',item.devotional.contexto],['❤️ 3. Aplicação — reflexão',item.devotional.aplicacao],['🚶 4. Prática — como viver isso hoje',item.devotional.pratica],['🙏 5. Oração',item.devotional.oracao]];
 els.devotional.innerHTML=labels.map(([h,p])=>`<article class="devotional-section"><h3>${h}</h3><p>${escapeHtml(p)}</p></article>`).join('');
 const pct=Math.min(100,Math.round((completed.length/TOTAL_ETAPAS)*100)); els.percent.textContent=pct+'%'; els.bar.style.width=pct+'%';
 els.progressText.textContent=completed.length?`${completed.length} etapa${completed.length===1?'':'s'} concluída${completed.length===1?'':'s'}. Próxima: ${item.range}.`:'Começando em Levítico 1.';
 els.complete.textContent=completed.includes(item.id)?'✓ Leitura já concluída':'✓ Marcar leitura como concluída';
}
function escapeHtml(s){return s.replace(/[&<>"']/g,c=>({'&':'&amp;','<':'&lt;','>':'&gt;','"':'&quot;',"'":'&#039;'}[c]))}
function complete(){const state=getState();const item=LEITURAS[state.index]||LEITURAS[0];const done=getCompleted();if(!done.includes(item.id))done.push(item.id);saveCompleted(done);if(state.index<LEITURAS.length-1){state.index++;localStorage.setItem(ESTADO_KEY,JSON.stringify(state));}render();window.scrollTo({top:0,behavior:'smooth'})}
function history(){els.home.classList.add('hidden');els.history.classList.remove('hidden');const done=getCompleted();if(!done.length){els.historyList.innerHTML='<div class="empty">Nenhuma leitura concluída ainda.</div>';return}els.historyList.innerHTML=done.slice().reverse().map(id=>{const x=LEITURAS.find(y=>y.id===id);return x?`<div class="history-item"><strong>${x.range}</strong><small>Estudo concluído</small></div>`:''}).join('')}
function back(){els.history.classList.add('hidden');els.home.classList.remove('hidden')}
function speak(){const item=LEITURAS[getState().index]||LEITURAS[0];const text=`${item.title}. Explicação do texto. ${item.devotional.explicacao}. Contexto. ${item.devotional.contexto}. Aplicação. ${item.devotional.aplicacao}. Prática. ${item.devotional.pratica}. Oração. ${item.devotional.oracao}`;speechSynthesis.cancel();const u=new SpeechSynthesisUtterance(text);u.lang='pt-BR';u.rate=.95;speechSynthesis.speak(u)}
els.complete.addEventListener('click',complete);els.readAloud.addEventListener('click',speak);document.getElementById('historyBtn').addEventListener('click',history);document.getElementById('backBtn').addEventListener('click',back);render();