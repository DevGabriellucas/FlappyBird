# Flappy Bird em Java

Este projeto é uma versão simples do clássico Flappy Bird, desenvolvida em Java com interface gráfica. O objetivo é controlar o pássaro, desviar dos canos e tentar alcançar a maior pontuação possível.

O jogo foi criado como prática de programação, trabalhando conceitos importantes como movimentação de objetos na tela, gravidade, colisões, eventos de teclado, pontuação e salvamento de recorde.

## Como o jogo funciona

O jogador controla o pássaro usando a tecla de espaço. Cada toque faz o pássaro subir, enquanto a gravidade o puxa para baixo constantemente. Para continuar jogando, é preciso passar entre os canos sem bater.

Quando o pássaro colide com um cano ou cai, a partida termina e a tela de Game Over é exibida. Nessa tela aparece o maior recorde alcançado.

## Pontuação e recorde

A pontuação aumenta conforme o jogador passa pelos canos. O recorde é salvo localmente, então mesmo depois de fechar e abrir o jogo novamente, a maior pontuação continua guardada.

Quando uma nova pontuação ultrapassa o recorde anterior, o valor antigo é substituído automaticamente pelo novo.

## Principais recursos

- Jogo com janela gráfica.
- Controle do pássaro pelo teclado.
- Movimento com gravidade.
- Canos gerados durante a partida.
- Detecção de colisão.
- Pontuação em tempo real.  
- Tela de Game Over.
- Recorde salvo localmente.

## Objetivo do projeto

O foco do projeto é praticar lógica de programação e desenvolvimento de jogos simples em Java. Ele também ajuda a entender melhor como funcionam interfaces gráficas, temporizadores, eventos do teclado e atualização constante da tela.
