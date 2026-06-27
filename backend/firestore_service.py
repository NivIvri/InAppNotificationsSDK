import firebase_admin
from firebase_admin import credentials, firestore
from datetime import date

db = None

def init_firestore():
    global db
    cred = credentials.Certificate("serviceAccountKey.json")
    firebase_admin.initialize_app(cred)
    db = firestore.client()

def get_all_notifications(screen=None, user_type=None):
    today = date.today().isoformat()
    query = db.collection("notifications").where("isActive", "==", True)

    results = []
    for doc in query.stream():
        data = doc.to_dict()
        data["id"] = doc.id
        if data.get("startDate", "") > today:
            continue
        if data.get("endDate", "") < today:
            continue
        if screen and data.get("targetScreen") not in (screen, "all"):
            continue
        if user_type and data.get("audience") not in (user_type, "all"):
            continue
        results.append(data)
    return results

def get_all_admin_notifications():
    ref = db.collection("notifications")
    results = []
    for doc in ref.stream():
        data = doc.to_dict()
        data["id"] = doc.id
        results.append(data)
    return results

def get_notification(notification_id):
    doc = db.collection("notifications").document(notification_id).get()
    if not doc.exists:
        return None
    data = doc.to_dict()
    data["id"] = doc.id
    return data

def create_notification(data):
    data.setdefault("isActive", True)
    data.setdefault("viewsCounter", 0)
    data.setdefault("clicksCounter", 0)
    ref = db.collection("notifications").document()
    ref.set(data)
    data["id"] = ref.id
    return data

def update_notification(notification_id, data):
    data.pop("id", None)
    ref = db.collection("notifications").document(notification_id)
    if not ref.get().exists:
        return None
    ref.update(data)
    updated = ref.get().to_dict()
    updated["id"] = notification_id
    return updated

def set_active(notification_id, is_active):
    ref = db.collection("notifications").document(notification_id)
    if not ref.get().exists:
        return None
    ref.update({"isActive": is_active})
    data = ref.get().to_dict()
    data["id"] = notification_id
    return data

def increment_views(notification_id):
    ref = db.collection("notifications").document(notification_id)
    if not ref.get().exists:
        return None
    ref.update({"viewsCounter": firestore.Increment(1)})
    return True

def increment_clicks(notification_id):
    ref = db.collection("notifications").document(notification_id)
    if not ref.get().exists:
        return None
    ref.update({"clicksCounter": firestore.Increment(1)})
    return True

def delete_notification(notification_id):
    ref = db.collection("notifications").document(notification_id)
    if not ref.get().exists:
        return False
    ref.delete()
    return True
