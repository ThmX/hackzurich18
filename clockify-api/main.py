from datetime import datetime
import requests

from google.cloud import datastore

API_KEY = 'W50DMrB5hw5YFtLi'
CLOCKIFY_API = 'https://api.clockify.me/api/'
WORKSPACE_ID = '5b9cffc4b079870e5816d189'
PROJECT_ID = '5b9d3332b079870e5816e710'

DICE_PROJECTS_IDS = {
    1: '5b9d3332b079870e5816e710',
    2: '5b9d5847b079870e5816f48a',
    3: '5b9d584bb079870e5816f492',
    4: '5b9d5851b079870e5816f499',
    5: '5b9d5857b079870e5816f49e'
}

headers = {
    'X-Api-Key': API_KEY
}


def clockify_api(path):
    return "{}{}".format(CLOCKIFY_API, path)


def start_time(project_id):
    url = clockify_api('/workspaces/{workspaceId}/timeEntries/'.format(
        workspaceId=WORKSPACE_ID
    ))

    current_task = {
        "start": datetime.utcnow().isoformat() + 'Z',
        "billable": True,
        "description": "My text 2",
        "projectId": project_id,
        "taskId": None,
        "tagIds": []
    }

    response = requests.post(url, headers=headers, json=current_task)

    last_response = response.json()
    current_task['taskId'] = last_response['id']

    client = datastore.Client()
    key = client.key('current_task', 'last')
    entity = datastore.Entity(key=key)
    entity.update(current_task)
    client.put(entity)


def end_time():
    client = datastore.Client()
    key = client.key('current_task', 'last')
    entity = datastore.Entity(key=key)
    current_task = client.get(key)
    current_task['end'] = datetime.utcnow().isoformat() + 'Z'
    current_task['description'] = 'My New Text 3'
    entity.update(current_task)

    requests.put(clockify_api(
        '/workspaces/{workspaceId}/timeEntries/{taskId}'.format(
            workspaceId=WORKSPACE_ID,
            taskId=current_task['taskId']
        )), json=current_task,
        headers=headers)


def rotate_dice(new_side):
    client = datastore.Client()
    key = client.key('current_side', 'last')
    entity = datastore.Entity(key=key)
    current_side = client.get(key)

    if current_side and 'side' in current_side and current_side['side'] == new_side:
        return

    end_time()

    if new_side > 0:
        start_time(DICE_PROJECTS_IDS[new_side])

    entity.update({
        'side': new_side
    })


def endpoint_start_stop_time(request):
    request_json = request.get_json()
    action = request_json['queryResult']['action']

    if action == 'start_time':
        if 'projectId' in request_json:
            project_id = request_json['projectId']
        else:
            project_id = PROJECT_ID

        start_time(project_id)

    elif action == 'end_time':
        end_time()

    return 'Ok'


def endpoint_rotate_dice(request):
    request_json = request.get_json()
    new_side = request_json['side']

    rotate_dice(new_side)
    return 'Ok'
