from rest_framework import serializers
from ..models import Food

class FoodListSerializer(serializers.ModelSerializer):
    
    class Meta:
        model = Food
        fields = ('__all__')

class FoodSerializer(serializers.ModelSerializer):

    class Meta:
        model = Food
        fields = ('__all__')