from django.urls import path, include
from . import views

# /v1/api/foods/

urlpatterns = [
    path('', views.food_list_create),
    path('<int:food_id>/', views.food_read_update_delete),
    path('setdb/', views.food_setdb)
]
