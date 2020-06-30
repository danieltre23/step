// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

function openModal(num) {
    $('#modalMyDog').modal('show'); 
    document.getElementById("modalMyDogImg").src = 'images/cuky'+num+'.jpg';
}

function fetchFunction() {
    const maxComments = document.getElementById('maxComments').value;
    fetch('/data?maxComments='+maxComments).then(response => response.json()).then((data) => {
        const commentsList = document.getElementById('commentsList');
        commentsList.innerHTML = "";
        data.forEach(element => {
            commentsList.appendChild(createNewElement(element));
        });
    })
}

function createNewElement({emoji, name, text, id}) {
    const newElement = document.createElement('div');
    newElement.setAttribute('class', 'media margin10px');
    newElement.setAttribute('id', id);
    newElement.innerHTML = `
        <img class='margin10px' ${emoji == 1 ? ("src='icons/emoji-smile.svg'") : 
            (emoji == 2 ? "src='icons/emoji-neutral.svg'" : "src='icons/emoji-frown.svg'")} 
            width="32" height="32" color/>
        <div class='media-body'>
            <h5 class='mt-0'>${name}</h5>
            ${text}
        </div>
    `;
    return newElement;
}

function deleteComments() {
    const request = new Request('/delete-data', {method: 'POST'});
    fetch(request).then(response => {
        if(response.status = 200){
            fetchFunction();
        }else{
            console.log(response);
        }
    })
}
