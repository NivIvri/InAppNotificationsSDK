from flask import Blueprint, request, jsonify
import firestore_service as fs

api = Blueprint("api", __name__)


@api.route("/api/notifications", methods=["GET"])
def get_notifications():
    screen = request.args.get("screen")
    user_type = request.args.get("userType")
    notifications = fs.get_all_notifications(screen=screen, user_type=user_type)
    return jsonify({"notifications": notifications}), 200


@api.route("/api/admin/notifications", methods=["GET"])
def get_admin_notifications():
    notifications = fs.get_all_admin_notifications()
    return jsonify({"notifications": notifications}), 200


@api.route("/api/notifications/<notification_id>", methods=["GET"])
def get_notification(notification_id):
    notification = fs.get_notification(notification_id)
    if notification is None:
        return jsonify({"error": "Notification not found"}), 404
    return jsonify(notification), 200


@api.route("/api/notifications", methods=["POST"])
def create_notification():
    data = request.get_json()
    if not data:
        return jsonify({"error": "Request body is required"}), 400
    required = ["title", "message", "targetScreen", "audience", "startDate", "endDate"]
    for field in required:
        if field not in data:
            return jsonify({"error": f"Missing required field: {field}"}), 400
    notification = fs.create_notification(data)
    return jsonify(notification), 201


@api.route("/api/notifications/<notification_id>", methods=["PUT"])
def update_notification(notification_id):
    data = request.get_json()
    if not data:
        return jsonify({"error": "Request body is required"}), 400
    notification = fs.update_notification(notification_id, data)
    if notification is None:
        return jsonify({"error": "Notification not found"}), 404
    return jsonify(notification), 200


@api.route("/api/notifications/<notification_id>/activate", methods=["PATCH"])
def activate_notification(notification_id):
    notification = fs.set_active(notification_id, True)
    if notification is None:
        return jsonify({"error": "Notification not found"}), 404
    return jsonify(notification), 200


@api.route("/api/notifications/<notification_id>/deactivate", methods=["PATCH"])
def deactivate_notification(notification_id):
    notification = fs.set_active(notification_id, False)
    if notification is None:
        return jsonify({"error": "Notification not found"}), 404
    return jsonify(notification), 200


@api.route("/api/notifications/<notification_id>/view", methods=["PATCH"])
def track_view(notification_id):
    result = fs.increment_views(notification_id)
    if result is None:
        return jsonify({"error": "Notification not found"}), 404
    return jsonify({"success": True}), 200


@api.route("/api/notifications/<notification_id>/click", methods=["PATCH"])
def track_click(notification_id):
    result = fs.increment_clicks(notification_id)
    if result is None:
        return jsonify({"error": "Notification not found"}), 404
    return jsonify({"success": True}), 200


@api.route("/api/notifications/<notification_id>", methods=["DELETE"])
def delete_notification(notification_id):
    deleted = fs.delete_notification(notification_id)
    if not deleted:
        return jsonify({"error": "Notification not found"}), 404
    return jsonify({"success": True}), 200
