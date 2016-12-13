from django.shortcuts import render,render_to_response

from django.http import HttpResponse
from django.template.loader import get_template

from django.template import Context
from django.core.exceptions import *
from django.views.generic.base import TemplateView

from django import forms
from django.template import loader
from django.shortcuts import render
from django.http import HttpResponseRedirect

from Yelp.model_interface import *

# Create your views here.

def index(request):
	"""state = ["Arizona","Philadelphia"]
	t = get_template('form.html')
	return HttpResponse(t.render({'state':state},request))"""
	return render(request,'form.html')

def search(request):
    # if this is a POST request we need to process the form data
	if request.method == 'POST':
		# create a form instance and populate it with data from the request:
		#form1 = forms.CharField(label = 'Enter the city name', max_length=100)
		city_name = request.POST.get('textfield', None)
		print(city_name)
		# check whether it's valid:
		    #process the data in form.cleaned_data as required
		t = ""
		if(city_name=="AZ"):
			    OutputName = predict_best_restraunt("AZ")
			    t = get_template(OutputName)
		elif(city_name=="PA"):
			    OutputName = predict_best_restraunt("PA")
			    t = get_template(OutputName)
		else:	
			    t = get_template('form.html')
		return HttpResponse(t.render({'name':city_name},request))
	else:
		return render(request, 'form.html')



