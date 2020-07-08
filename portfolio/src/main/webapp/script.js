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

let commentsArray = [];
let start = 0;

function openModal(num) {
    $('#modalMyDog').modal('show'); 
    document.getElementById("modalMyDogImg").src = 'images/cuky'+num+'.jpg';
}

function fetchFunction() {
    fetch('/data').then(response => response.json()).then((data) => {
        commentsArray = data.comments;
        start = 0;
        if (commentsArray.length === 0) {
            document.getElementById('commentDependent').setAttribute('class', 'hide');
        }
        assginCommentsToUI();

        if (!data.user) {
            document.getElementById('commentFormDiv').setAttribute('class', 'hide');
            document.getElementById('loginBtn').setAttribute('href', data.url);
        } else {
            document.getElementById("commentUser").value = data.user;
            document.getElementById("hiddenUser").value = data.user;
            document.getElementById('commentFormDiv').setAttribute('class', 'text-center padding-10-px');
            document.getElementById('loginDiv').setAttribute('class', 'hide');
            document.getElementById('logoutBtn').setAttribute('href', data.url);
        }

        document.getElementById("commentForm").action = data.uploadImageUrl;

    })
}

function assginCommentsToUI() {
    if (start === 0) {
        document.getElementById('arrowUp').setAttribute('class', 'hide');
    } else {
        document.getElementById('arrowUp').setAttribute('class', 'justifyCenter');
    }
    const maxComments = parseInt(document.getElementById('maxComments').value);
    if (commentsArray.length <= start+maxComments) {
        document.getElementById('arrowDown').setAttribute('class', 'hide');
    } else {
        document.getElementById('arrowDown').setAttribute('class', 'justifyCenter');
    } 
    const commentsList = document.getElementById('commentsList');
    commentsList.innerHTML = '';
    for (let i = start; i < start+maxComments && i < commentsArray.length; i++) {
        commentsList.appendChild(createNewElement(commentsArray[i]));
        commentsList.appendChild(document.createElement('hr'));
    }
}

function createNewElement({key, emoji, name, text, id, imageUrl}) {
    const newElement = document.createElement('div');
    newElement.setAttribute('class', 'media margin-10-px');
    newElement.setAttribute('id', id);
    newElement.innerHTML = `
        <img class='margin-10-px' ${emoji == 1 ? ("src='icons/emoji-smile.svg'") : 
            (emoji == 2 ? "src='icons/emoji-neutral.svg'" : "src='icons/emoji-frown.svg'")} 
            width="32" height="32"/>
        <div class='media-body margin-10-px'>
            <h5 class='mt-0'>${name}</h5>
            ${text}
            ${imageUrl ? ` <a href="${imageUrl}"> <img src="${imageUrl}"/> <a/>` : null}
        </div>
        <button class="btn" onClick="deleteCommentByKey('${key}')">
            <img class='auto-margin' src='icons/trash.svg' width="20" height="20"/>
        </button>
    `;
    return newElement;
}

function deleteComments() {
    const request = new Request('/delete-data', {method: 'POST'});
    fetchRequestAndReload(request);
}

function deleteCommentByKey(key) {
    const request = new Request('/delete-comment', {method: 'POST', body: key} );
    fetchRequestAndReload(request);
}

function fetchRequestAndReload(request) {
    fetch(request).then(response => {
        if (response.status === 200) {
            fetchFunction();
        } else {
            console.error(response);
        }
    })
}

function changeCommentPage(forward) {
    const maxComments = parseInt(document.getElementById('maxComments').value);
    if (forward) {
        start += maxComments;
    } else {
        start -= maxComments;
    }
    assginCommentsToUI();
}