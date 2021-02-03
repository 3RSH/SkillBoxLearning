$(function(){

    //Добавление дела в список дел
    const appendTodo = function(data) {

        //формируем блок элементов для списка
        var todoCode = '<h4>' + data.id + '. ' + data.name
        + ' ' + '<button id="complete-todo" value="'
        + data.id + '">Выполнить</button>'
        + ' ' + '<button id="delete-todo" value="'
                + data.id + '">Удалить</button>'
        + '</h4>'
        + 'Статус: ' + statusTxt(data);

        //добавляем сфорированный блок
        $('#todo-list')
            .append('<div>' + todoCode + '</div>');
    };

    //Определение статуса дела
    var statusTxt = function(data) {
        if (data.complete) return 'Выполнено';
        return 'Не выполнено';
    };

//    //Загузка дел в список страницы
//    $.get('/doings/', function(response) {
//        for(i in response) {
//            appendTodo(response[i]);
//        }
//    });

    //Обработка нажатия кнопки добавления дела
    $('#show-add-todo-form').click(function() {
        $('#todo-form').css('display', 'flex');
    });

    //Скрытие формы добавления дела
    $('#todo-form').click(function(event) {
        if(event.target === this) {
            $(this).css('display', 'none');
        }
    });

    //Добавление дела
    $('#save-todo').click(function() {
        var data = $('#todo-form form').serialize();
        $.ajax( {
            method: "POST",
            url: '/doings/',
            data: data,
            success: function(response) {
                $('#todo-form').css('display', 'none');

                var todo = {};
                todo.id = response;
                var dataArray = $('#todo-form form').serializeArray();

                for(i in dataArray) {
                    todo[dataArray[i]['name']] = dataArray[i]['value'];
                }
                window.location.reload();
            }
        });
        return false;
    });

    //Обработка нажатия кнопки очистки списка дел
    $('#clear-todo-list').click(function() {
        $.ajax( {
            url: '/doings/',
            type: 'DELETE',
            success: function(result) {
                window.location.reload();
            }
        });
    });

    //Обработка нажатия кнопки выполнения дела
    $(document).on('click', '#complete-todo', function() {
        var todoId = this.value;

        $.ajax( {
            method: "PUT",
            url: '/doings/' + todoId,
            success: function(response) {
                window.location.reload();
            }
        });
    });

    //Обработка нажатия кнопки удаления дела
    $(document).on('click', '#delete-todo', function() {
        var todoId = this.value;
        $.ajax( {
            method: "DELETE",
            url: '/doings/' + todoId,
            success: function(response) {
                window.location.reload();
            }
        });
    });
});