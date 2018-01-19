const userIndex           = window.location.href + (window.location.href.endsWith('/') ? '' : '/');
const petIndex            = `${userIndex}pets/`;
const reservationIndex    =`${userIndex}reservations/`;
const getSpeciesAPI          = '/api/species';

const btnPets             = $('#btn-pets'           );
const btnReservations     = $('#btn-reservations'   );
const btnEdit             = $('#btn-edit-user'      );
const btnDelete           = $('#btn-delete-user'    );
const btnEmploy           = $('#btn-employ-user'    );
const btnFire             = $('#btn-fire-user'      );
const btnAddPet           = $('#btn-add-pet'        );
const btnAddReservation   = $('#btn-add-reservation');

const addPetModal         = $('#add-pet-modal'        );
const addReservationModal = $('#add-reservation-modal');

const userRole            = $('#role');

const clientLabel   = '<p class="ui client long tag label">Klijent</p>';
const employeeLabel = '<p class="ui employee long tag label">Zaposlenik</p>';

function hideElem(e){
    e.addClass('inactive');
}

function showElem(e) {
    e.removeClass('inactive');
}


function patch(data, onSuccess, onFail){
    $.ajax({
        url: userIndex,
        type: 'PATCH',
        contentType: "application/json; charset=utf-8",
        cache: false,
        processData: false,
        data: JSON.stringify(data)
    })
        .then(onSuccess)
        .catch(onFail)
}

const employOperation = {
    "op": "replace",
    "path": "/status",
    "value": "employee"
};

const fireOperation = {
    "op": "replace",
    "path": "/status",
    "value": "client"
};

let Table= function(indexUrl, table, tableBody, placeholder, deleteModal){
    this.indexUrl = indexUrl;
    this.table = table;
    this.tableBody = tableBody;
    this.placeholder = placeholder;
    this.deleteModal = deleteModal;
    this.isUpdating = false;
};

Table.prototype = {
    isEmpty:function(){
        return this.table.find('tbody tr').length === 0;
    },

    formatTableRow: function(...cells) {
        let result = '<tr>';
        result += cells.map(cell => `<td>${cell}</td>`).join('');
        result += '</tr>';
        return result;

    },

    getData: function(){
        return $.getJSON(this.indexUrl);
    },

    remove: function(row){
        const deleteUrl = `${this.indexUrl}${row.data('id')}/`;
        this.deleteModal
            .modal({
            onApprove: () => {
                $.ajax({
                    url: deleteUrl,
                    type: 'DELETE',
                }).then(() => {
                    row.remove();
                    if (this.isEmpty()) {
                        this.table.hideElem();
                        this.placeholder.show();
                    }
                })
            }
        })
            .modal('show');

    },

    update: function(){
        if(this.isUpdating){
            return;
        }
        this.isUpdating = true;
        this.tableBody.empty();
        this.getData()
            .then(entities => {
                if(entities.length===0){
                    this.table.hide();
                    this.placeholder.show();
                    return;
                }
                entities.forEach(entity => this.append(entity));
                this.placeholder.hide();
                this.table.show();

            })
            .fail(console.log)
            .always(
                () => this.isUpdating = false
            );
    },

    save: function(fields) {
        $.post({
            url: this.indexUrl,
            contentType: "application/json; charset=utf-8",
            cache: false,    //This will force requested pages not to be cached by the browser
            processData: false, //To avoid making query String instead of JSON
            data: JSON.stringify(fields)
        })
            .then(data => {
                this.append(data);
            })
            .catch(() => console.log("tu sam"));
    }
};



let petTable = new Table(
    petIndex,
    $('#pets-table'),
    $('#pets').find('tbody'),
    $('#pet-placeholder'),
    $('#delete-pet-modal')
);

let appendPet = function(pet){
    let deleteButton = '<i class="trash big remove action icon" title="Obriši ljubimca"></i>';
    let petMarkup = $(this.formatTableRow(pet.name, pet.age, pet.species,pet.sex, pet.microchip, pet.remark,deleteButton));

    petMarkup.data('id',pet.petId);
    petTable.tableBody.append(petMarkup);
    petMarkup.find('.remove.icon').click(petTable.remove.bind(petTable,petMarkup));

    this.placeholder.hide();
    this.table.show();
};

petTable.append = appendPet.bind(petTable);

let resTable = new Table(
    reservationIndex,
    $('#reservations-table'),
    $('#reservations').find('tbody'),
    $('#reservations-placeholder')
);

resTable.append = (function(res){
    let resMarkup = $(this.formatTableRow(res.pet, res.service, res.employee, res.status, res.time));
    resMarkup.data('id',resMarkup.reservationId);
    resTable.tableBody.append(resMarkup);
}).bind(resTable);

const petValidation = {
    name: {
        identifier: 'pet-name',
        rules: [{
            type: 'empty',
            prompt: 'Ljubimac mora imati ime'
        }]
    },
    age: {
        identifier: 'pet-age',
        rules: [{
            type: 'empty',
            prompt: 'Ljubimac mora imati broj godina'
        }]
    },
    sex: {
        identifier: 'gender',
        rules: [{
            type: 'empty',
            prompt: 'Molimo odredite spol'
        }]
    },
    species: {
        identifier: 'pet-species',
        rules: [{
            type: 'empty',
            prompt: 'Molimo odaberite vrstu ljubimca'
        }]
    },
    breed: {
        identifier: 'pet-breed'
    },
    microchip: {
        identifier: 'pet-chip'
    },
    remark:{
        identifier: 'remark'
    }
};

const reservationValidation = {
    service: {
        identifier: 'usluga',
        rules: [{
            type: 'empty',
            prompt: 'Molimo unesite opis usluge'
        }]
    },
    pet: {
        identifier: 'pet',
        rules: [{
            type: 'empty',
            prompt: 'Molimo odaberite svog ljubimca'
        }]
    },
    executionTime: {
        identifier: 'executionTime',
        rules: [{
            type: 'empty',
            prompt: 'Molimo unesite željeno vrijeme usluge'
        }]
    },

    duration: {
        identifier: 'duration',
        rules: [{
            type: 'empty',
            prompt: 'Molimo unesite trajanje usluge'
        }]
    },
};


btnPets.click(petTable.update.bind(petTable));

let petSpecies = $('#pet-species');
btnAddPet.click(() => {
        addPetModal.modal('show');
        $.getJSON(getSpeciesAPI)
            .then(
                data => {
                    petSpecies.empty();
                    let options = [];
                    data.forEach(x => {
                        options.push({
                            value: x.id,
                            text: x.name,
                            name: x.name
                        });
                        petSpecies.append($('<option>', {value:x.id, text:x.name}));
                    });
                    petSpecies.dropdown('setup menu', {values: options});
                }
            );
    }
);

btnAddReservation.click(
    () => window.location.href=reservationIndex+'new'
    //() => addReservationModal.modal('show')
);

btnReservations.click(resTable.update.bind(resTable));

btnDelete.click(function() {
    $('#delete-user-modal')
        .modal({
            closable  : false,
            onApprove : () => {
                $.ajax({
                    type: 'DELETE',
                    url: userIndex.substring(0,userIndex.length-1)
                })
                    .then(function() {
                            window.location.replace("/");
                        }
                    )
                    .catch(console.log);
            }
        })
        .modal('show')
    ;
});

btnEdit.click(
    () => window.location.href = `${userIndex}edit`
);



function switchButtons(toHide, toShow) {
    hideElem(toHide);
    showElem(toShow);
}

function hire(){
    switchButtons(btnEmploy, btnFire);
    userRole.find('p').remove();
    userRole.append(employeeLabel);
}

function fire(){
    switchButtons(btnFire, btnEmploy);
    userRole.find('p').remove();
    userRole.append(clientLabel);
}

btnEmploy.click(() => {
    patch(
        [employOperation],
        hire,
        () => console.log(`Ovo se poslalo: "${JSON.stringify([employOperation])}", ali nista od toga`),
    );
});

btnFire.click(() => {
    patch(
        [fireOperation],
        fire,
        () => console.log(`Ovo se poslalo: "${JSON.stringify([fireOperation])}", ali nista od toga`),
    );
});


function modalInit(modal, formFields, handler){
    modal.form({
        inline: false,
        fields: formFields,
        onSuccess: (event,fields) => {
            event.preventDefault();
            handler(fields);
            modal.modal('hide');
        }
    });

    $('.ui.dropdown')
        .dropdown();
}

$(document)
    .ready(function() {
        modalInit(addPetModal,petValidation,petTable.save.bind(petTable));

        modalInit(addReservationModal,reservationValidation,
            (fields) => addEntity(
                reservationIndex,
                appendReservation,
                console.log,
                fields,
            ));

        $('.menu .item')
            .tab({'onVisible':function(tabpath){
                if(tabpath==='first'){
                    return;
                }
                if(tabpath === 'second'){
                    petTable.update();
                    return;
                }
                if(tabpath === 'third'){
                    resTable.update();
                }
            }})
        ;
    });


$('.action.icon')
  .popup();

