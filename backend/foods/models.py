from django.db import models

class Food(models.Model):
    DESC_KOR = models.CharField(max_length=200, null=False)
    SERVING_WT = models.CharField(max_length=50, null=False)
    NUTR_CONT1 = models.CharField(max_length=50, null=False)
    NUTR_CONT2 = models.CharField(max_length=50, null=False)
    NUTR_CONT3 = models.CharField(max_length=50, null=False)
    NUTR_CONT4 = models.CharField(max_length=50, null=False)
    NUTR_CONT5 = models.CharField(max_length=50, null=True)
    NUTR_CONT6 = models.CharField(max_length=50, null=True)
    NUTR_CONT7 = models.CharField(max_length=50, null=True)
    NUTR_CONT8 = models.CharField(max_length=50, null=True)
    NUTR_CONT9 = models.CharField(max_length=50, null=True)
    ANIMAL_PLANT = models.CharField(max_length=100, blank=True)