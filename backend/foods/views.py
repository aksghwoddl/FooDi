from pprint import pprint

import requests

from foods.serializers.food import FoodListSerializer, FoodSerializer

from .models import Food
from django.shortcuts import get_object_or_404
from rest_framework.response import Response
from rest_framework import status
from rest_framework.decorators import api_view

import os 

# Create your views here.
@api_view(['GET'])
def food_setdb(request):
    URL = 'http://apis.data.go.kr/1471000/FoodNtrIrdntInfoService1/getFoodNtrItdntList1'  
    ServiceKey = os.environ.get("API_KEY")

    # 50 제외
    for pageNo in range(51, 228):
        params = {
            'ServiceKey': ServiceKey,
            'pageNo': pageNo,
            'numOfRows': 100,
            'type': 'json'
        }

        response = requests.get(URL, params=params)
        response_json = response.json()
        food_data_list = response_json['body']['items']
        
        for food_data in food_data_list:
            serializer = FoodSerializer(data=food_data)
            if serializer.is_valid(raise_exception=True):
                if not Food.objects.filter(DESC_KOR=food_data['DESC_KOR']).exists():
                    serializer.save()
                    print('save!!')

    return Response(status=status.HTTP_200_OK)

@api_view(['GET', 'POST'])
def food_list_create(request):
    if request.method == 'GET':
        desc_kor = request.GET.get('desc_kor')
        pageNo = int(request.GET.get('pageNo'))
        animal_plant = request.GET.get('animal_plant')
        foods = Food.objects.all().filter(DESC_KOR__contains=desc_kor)
        totalCount = foods.count() // 10
        if totalCount % 10:
            totalCount += 1
        serializer = FoodListSerializer(foods[(pageNo - 1) * 10:pageNo * 10], many=True)
        response = dict()
        response['totalCount'] = totalCount
        response['pageNo'] = pageNo
        response['results'] = serializer.data
        return Response(response)
    
    elif request.method == 'POST':
        serializer = FoodSerializer(data=request.data)
        if serializer.is_valid(raise_exception=True):
            serializer.save()
            return Response(status=status.HTTP_201_CREATED)
        return Response(status=status.HTTP_400_BAD_REQUEST)

@api_view(['GET', 'PATCH', 'DELETE'])
def food_read_update_delete(request, food_id):
    food = get_object_or_404(Food, pk=food_id)
    
    if request.method == 'GET':
        serializer = FoodSerializer(food)
        return Response(serializer.data)
    
    elif request.method == 'PATCH':
        serializer = FoodSerializer(food, data=request.data)
        if serializer.is_valid(raise_exception=True):
            serializer.save()
            return Response(serializer.data)
        

    elif request.method == 'DELETE':
        food.delete()
        return Response(status=status.HTTP_204_NO_CONTENT)