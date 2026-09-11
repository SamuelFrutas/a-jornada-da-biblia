const chapters=[
 {id:1,title:'A Criação',ref:'Gênesis 1',summary:'O início da história: Deus cria os céus, a terra e tudo o que nela existe.',xp:50,unlock:null,discoveries:[['A criação','Gênesis 1','O relato apresenta Deus como Criador e organiza a criação em uma sequência de dias.']],question:{q:'Segundo Gênesis 1, o que Deus criou no princípio do relato?',options:['A luz','O templo','Jerusalém','O povo de Israel'],correct:0,explain:'O capítulo começa apresentando a criação da luz. Confira o relato completo em Gênesis 1:3.',ref:'Gênesis 1:3'}},
 {id:2,title:'O Jardim do Éden',ref:'Gênesis 2',summary:'O ser humano é colocado no jardim, recebe uma missão e conhece o mandamento de Deus.',xp:60,unlock:1,discoveries:[['Éden','Gênesis 2','O jardim é apresentado como o lugar onde Deus colocou o ser humano no início da narrativa.'],['Adão','Gênesis 2','O primeiro homem recebe uma tarefa e vive no jardim antes da queda.']],question:{q:'Qual era o jardim onde o homem foi colocado?',options:['Jardim do Éden','Jardim de Jerusalém','Jardim do Sinai','Jardim de Belém'],correct:0,explain:'O relato de Gênesis 2 chama esse lugar de jardim do Éden.',ref:'Gênesis 2:8'}},
 {id:3,title:'A Queda',ref:'Gênesis 3',summary:'A desobediência entra na história e o relacionamento do ser humano com Deus é marcado por suas consequências.',xp:70,unlock:2,discoveries:[['A serpente','Gênesis 3','A serpente aparece no relato como personagem da tentação que conduz à desobediência.'],['A queda','Gênesis 3','A desobediência de Adão e da mulher traz consequências e encerra sua permanência no jardim.']],question:{q:'Qual foi a consequência imediata narrada após a desobediência?',options:['Eles foram expulsos do jardim','Eles construíram uma cidade','Eles voltaram para o Egito','Eles receberam uma coroa'],correct:0,explain:'O capítulo termina com a expulsão do jardim do Éden e a guarda do caminho para a árvore da vida.',ref:'Gênesis 3:23–24'}}
];

const defaultState={xp:0,completed:[],discoveries:[]};
let state=JSON.parse(localStorage.getItem('jornadaBiblia')||'null')||defaultState;
let current=null;

const $=s=>document.querySelector(s);
function save(){localStorage.setItem('jornadaBiblia',JSON.stringify(state));}
function show(id){document.querySelectorAll('.screen').forEach(x=>x.classList.remove('active'));$('#'+id).classList.add('active');window.scrollTo(0,0);}
function renderHeader(){
 const level=Math.floor(state.xp/100)+1, inLevel=state.xp%100;
 $('#levelText').textContent='Nível '+level;
 $('#xpText').textContent=state.xp+' XP';
 $('#xpFill').style.width=Math.min(inLevel,100)+'%';
}
function renderMap(){
 renderHeader();
 $('#chapterNodes').innerHTML=chapters.map(c=>{
  const done=state.completed.includes(c.id), unlocked=c.unlock===null||state.completed.includes(c.unlock);
  return `<article class="chapter-card ${unlocked?'':'locked'}"><div class="node">${done?'✓':c.id}</div><div><h3>Capítulo ${c.id} — ${c.title}</h3><p>${c.summary}</p></div>${unlocked?`<button data-chapter="${c.id}">${done?'REVISITAR':'ENTRAR'}</button>`:'<span>🔒</span>'}</article>`;
 }).join('');
 document.querySelectorAll('[data-chapter]').forEach(b=>b.onclick=()=>openChapter(Number(b.dataset.chapter)));
}
function openChapter(id){
 current=chapters.find(c=>c.id===id);show('chapter');
 $('#chapterLabel').textContent='GÊNESIS';$('#chapterProgress').textContent=`Capítulo ${current.id} de 3`;
 $('#chapterXp').textContent=`+${current.xp} XP`;
 $('#chapterBody').innerHTML=`<article class="story-card"><div class="chapter-number">${current.ref.toUpperCase()}</div><h2>${current.title}</h2><p>${current.summary}</p><span class="reference">📖 ${current.ref}</span><div class="question"><h3>Desafio da jornada</h3><p>${current.question.q}</p><div class="answers">${current.question.options.map((o,i)=>`<button class="answer" data-answer="${i}">${String.fromCharCode(65+i)}) ${o}</button>`).join('')}</div><div id="feedback"></div></div></article>`;
 document.querySelectorAll('[data-answer]').forEach(b=>b.onclick=()=>answer(Number(b.dataset.answer)));
}
function answer(choice){
 const q=current.question, buttons=document.querySelectorAll('[data-answer]');
 buttons.forEach(b=>b.disabled=true);
 buttons[q.correct].classList.add('correct');
 if(choice!==q.correct)buttons[choice].classList.add('wrong');
 const ok=choice===q.correct;
 $('#feedback').innerHTML=`<div class="feedback"><strong class="${ok?'success':'danger'}">${ok?'✓ Correto!':'Quase!'}</strong><br>${q.explain}<br><span class="reference">${q.ref}</span><br><button class="next" id="finishBtn">${current.id===3?'CONCLUIR JORNADA':'CONCLUIR CAPÍTULO'}</button></div>`;
 $('#finishBtn').onclick=finishChapter;
}
function finishChapter(){
 if(!state.completed.includes(current.id)){state.completed.push(current.id);state.xp+=current.xp;current.discoveries.forEach(d=>{if(!state.discoveries.some(x=>x[0]===d[0]))state.discoveries.push(d)});save();}
 renderMap();show('map');
}
function renderLibrary(){
 const cards=state.discoveries.map(d=>`<article class="library-card"><h3>${d[0]}</h3><small>${d[1]}</small><p>${d[2]}</p></article>`).join('');
 $('#libraryBody').innerHTML=cards?`<div class="library-grid">${cards}</div>`:`<div class="empty">Complete capítulos para desbloquear descobertas.</div>`;
 show('library');
}
function reset(){if(confirm('Reiniciar todo o progresso desta jornada?')){state={...defaultState};save();renderMap();show('map');}}
$('#startBtn').onclick=()=>{state={...defaultState};save();renderMap();show('map')};
$('#continueBtn').onclick=()=>{renderMap();show('map')};
document.querySelectorAll('[data-action]').forEach(b=>b.onclick=()=>{const a=b.dataset.action;if(a==='map'){renderMap();show('map')}if(a==='library')renderLibrary();if(a==='reset')reset()});
renderHeader();
