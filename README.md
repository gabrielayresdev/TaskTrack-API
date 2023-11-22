# TaskTrack (API)

API desenvolvida para o projeto fullstack TaskTrak. A API consiste em duas partes principais: um sistema de login e cadastro com autenticação de usuário e geração de token JWT e um CRUD de uma todolist.

## Instalação

Tenha certeza de que o JDK, JVM e o Git estão instalados na máquina. Execute os seguintes comandos no terminal.

```bash
    git clone https://github.com/gabrielayresdev/TaskTrack-API
```

```bash
    mvn spring-boot:run
```

## endpoints 

- `/auth/register` | Cadastra um usuário
```js 
    Template do body: 
    {
        "username": "gabriel.ar.fort@gmail.com",
        "name": "Gabriel Ayres",
        "password": "gabriel123",
        "groups": ["Personal"],
        "telephone": "5521999999999",
        "role": "USER"
    }
```
- `/auth/login` | Valida o usuário e retorna os dados do usuário e um token JWT
```js 
    Template do body: 
    {
        "username": "gabriel.ar.fort@gmail.com",
        "password": "gabriel123"
    }
```
`Os próximos endpoints devem conter um token JWT no header Authorization`
- `/auth/validate` | Retorna os dados do usuário passando um token JWT no header
- `/task/create` | Cria uma tarefa
```js 
    Template do body: 
    {
        "title": "Desenvolver página de perfil",
        "description": "Criar a interface de perfil do usuário com foto e informações",
        "endAt": "2023-11-05T18:00:00",
        "priority": "Medium",
        "taskGroup": "Personal"
    }
```
- `/task/list` | Exibe as tarefas de cada usuário
- `/task/remove/${id}` | Exibe as tarefas de cada usuário
- `/task/update/${id}` | Atualiza uma tarefa
```js 
    Template do body: 
    {
        "title": "Atualizar readme do projeto",
    }
```
