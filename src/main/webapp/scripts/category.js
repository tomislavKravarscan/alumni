function deleteCategory(categoryId) {
    var r = confirm("Jeste li sigurni da želite obrisati kategoriju?");
    if (r == true) {
        $.ajax({
            type: 'POST',
            url: '/categories/' + categoryId + '/delete',
            complete: (obj, textStatus) => {
                if(textStatus === 'error') {
                    alert('Kategorija nije obrisana, vjerojatno postoje postovi koji su te kategorije.');
                    return;
                }

                alert('Kategorija uspješno obrisana.');
                window.location.href = "/users";
            }
        });
    }
}

$('.message .close')
.on('click', function () {
    $(this)
        .closest('.message')
        .transition('fade')
        ;
})
;

const rules = {
    name: {
        identifier  : 'name',
        rules: [
            {
                type   : 'empty',
                prompt : 'Upišite naziv'
            }
        ]
    }
};

$(document)
    .ready(function () {
        $('.ui.form')
            .form({
                inline: true,
                fields: rules,
                onSuccess: () => $('.submit.button').addClass('loading')
            })
            ;
    })
    ;
    
