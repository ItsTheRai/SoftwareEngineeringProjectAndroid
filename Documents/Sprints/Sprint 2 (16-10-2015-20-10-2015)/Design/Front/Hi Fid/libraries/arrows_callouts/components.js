/********************************************************************************************/
/********************************************************************************************/
/**************************            ARROWS CALLOUTS             **************************/
/********************************************************************************************/
/********************************************************************************************/

/************************************* COMMON COMPONENT VARIABLES AND PROPERTIES *************************************/
var _library = 'arrows_callouts';
var _path = '/arrows_callouts/';

/************************************* COMPONENT TYPES *************************************/



//TYPE: ARROWS
prx.types.arrow = {
	name: "arrow"
	,onDisplay: function(item,containerid) {
		
		var _id = (!containerid) ? item.id : containerid+'-'+item.id;
		var dims = prx.componentsHelper.getRealDims(item);

		if(item.filledArrow == "true") { item.filledArrow = true; }
		if(item.filledArrow == "false") { item.filledArrow = false; }
		if(item.isDashed == "true") { item.isDashed = true; }
		if(item.isDashed == "false") { item.isDashed = false; }

		var cR = '<div id="' + _id + '" class="box pos type-arrow '+item.arrowType+'" ';
		cR += 'data-border-width="'+item.borderWidth+'" data-dashed="'+item.isDashed+'" data-filled="'+item.filledArrow+'" ';
		cR += 'data-direction-arrow="'+item.arrowDirection+'">';
		
		cR += '<style>';
		cR += '.path-'+_id+' { stroke:'+prx.utils.getColor(item.borderColor)+'; stroke-width:'+item.borderWidth+'px; stroke-linejoin:'+item.joinType+'; stroke-linecap:'+item.capType+'; width:100%; height:100%; }';
		if( item.filledArrow) {
			cR += '#arrowPath-'+_id+' { fill: '+prx.utils.getColor(item.borderColor)+'; }';
		}
		else {
			cR += '#arrowPath-'+_id+' { fill: none; }';
		}
		if (item.arrowName == "arrow3") {
			cR += '#detailPath-'+_id+' { fill: '+prx.utils.getColor(item.borderColor)+'; }';
		}
		else {
			cR += '#detailPath-'+_id+' { fill: none; }';
		}
		cR += '</style>';
						
		cR += '<div id="'+item.arrowType+'-' + _id + '" class="arrow-wrapper ">';
		
		cR += '<svg id="svg-'+ _id +'" viewBox="0 0 '+(dims.width)+' '+(dims.height)+'" width="100%" height="100%" preserveAspectRatio="none" xmlns="http://www.w3.org/2000/svg" version="1.1">';
		
		if(item.arrowType == 'curved' || item.arrowType == 'arced' || item.arrowType == 'right-angle') {
			var path = prx.componentsHelper.drawArrowCurved( item, item.arrowType, dims.width, dims.height, item.borderWidth);
		}
		else if(item.arrowType == 'custom') {
			var path = prx.componentsHelper.drawArrowCustom( item, dims.width, dims.height, item.borderWidth);
		}
		else {
			var path = prx.componentsHelper.drawArrow( item, dims.width, dims.height, item.borderWidth);
		}			
		
		if(item.arrowType == 'custom') {
			cR += '<path d="'+path+'" id="customPath-'+ _id +'" class="liveUpdate-borderColor-stroke liveUpdate-borderColor-fill changeProperty-borderColor-stroke changeProperty-borderColor-fill changeProperty-borderWidth"';
				cR += 'style="fill: '+prx.utils.getColor(item.borderColor)+';" />';
		}
		else {
			var linePath = path.split("; ")[0];
			var arrowPath = path.split("; ")[1];
			
			// arrow lines
			cR += '<path d="'+linePath+'" id="linePath-'+ _id +'" class="path-'+ _id +' liveUpdate-borderColor-stroke changeProperty-borderColor-stroke changeProperty-borderWidth"';
				cR += 'style="fill: none; '+((item.isDashed) ? " stroke-dasharray: "+(5*(item.borderWidth/3))+","+(10*(item.borderWidth/3))+";" : "")+'" />';
				
			// arrow details (if any)
			if( item.arrowName == 'arrow2' || item.arrowName == 'arrow3' ) {
				var detailPath = path.split("; ")[2];
				cR += '<path d="'+detailPath+'" id="detailPath-'+ _id +'" class="path-'+ _id +' liveUpdate-borderColor-stroke changeProperty-borderColor-stroke '+((item.arrowName == 'arrow3') ? "liveUpdate-borderColor-fill changeProperty-borderColor-fill " : "")+'changeProperty-borderWidth" />';
			}
			
			// arrow heads
			cR += '<path d="'+arrowPath+'" id="arrowPath-'+ _id +'" class="path-'+ _id +' liveUpdate-borderColor-stroke changeProperty-borderColor-stroke changeProperty-borderWidth'+((item.filledArrow) ? " liveUpdate-borderColor-fill changeProperty-borderColor-fill" : "")+'" />';
		}
			
		cR += '</svg>';
		
		cR += '</div>';
		cR += '</div>';
		return cR;
	}
	,onResize: function(item,containerid) {

		var _id = (!containerid) ? item.id : containerid+'-'+item.id;
		var dims = prx.componentsHelper.getRealDims(item);	

		if(item.filledArrow == "true") { item.filledArrow = true; }
		if(item.filledArrow == "false") { item.filledArrow = false; }
		if(item.isDashed == "true") { item.isDashed = true; }
		if(item.isDashed == "false") { item.isDashed = false; }

		document.getElementById('svg-'+ _id).setAttribute('viewBox', '0 0 '+(dims.width)+' '+(dims.height));
		
		if(item.arrowType == 'curved' || item.arrowType == 'arced' || item.arrowType == 'right-angle') {
			var path = prx.componentsHelper.drawArrowCurved( item, item.arrowType, dims.width, dims.height, item.borderWidth);
		}
		else if(item.arrowType == 'custom') {
			var path = prx.componentsHelper.drawArrowCustom( item, dims.width, dims.height, item.borderWidth);
		}
		else {
			var path = prx.componentsHelper.drawArrow( item, dims.width, dims.height, item.borderWidth);
		}

		if(item.arrowType == 'custom') {
			document.getElementById('customPath-'+ _id).setAttribute('d', path);
		}
		else {
			var linePath = path.split("; ")[0];
			var arrowPath = path.split("; ")[1];
			
			document.getElementById('linePath-'+ _id).setAttribute('d', linePath);
			if( item.arrowName == 'arrow2' || item.arrowName == 'arrow3' ) {
				var detailPath = path.split("; ")[2];	
				document.getElementById('detailPath-'+ _id).setAttribute('d', detailPath);
			}
			document.getElementById('arrowPath-'+ _id).setAttribute('d', arrowPath);
		}
	}
	,interactions: [
		prx.commonproperties.actions
	]
	,propertyGroups: [		
        {
            caption: 'Style',
            properties: [
                 [
                     {
 	                    caption: 'Color',
 	                    name: 'borderColor',
 	                    proptype: 'border-color',
 	                    type: 'colorpicker',
 	                    value: function (item, name) {
 	                        return item.borderColor;
 	                    },
 	                    liveUpdate: 'stroke,fill',
 	                    changeProperty: {
 	                        caption: 'Arrow Color',
 	                        selector: '.changeProperty-borderColor',
 	                        property: 'stroke,fill',
 	                        transitionable: true
 	                    }
 	                }
                ],
                [
                 	{ 
                    	caption: 'Size', 
                    	name: 'borderWidth', 
                    	proptype: 'border-width', 
                    	type: 'combo-select', 
                    	value: function(item,name) { 
                    		return item.borderWidth; 
                    	}, 
                    	values: { min: 1, max: 15, step: 1 },
                    	changeProperty: { 
                    		caption: 'Size', 
                    		rerender: true
                    	} 
                    }
             	],
             	[
	                {
						caption: 'Dashed?'
						,name: 'isDashed'
						,type: 'onoff'
						,value: function(item,name) {
							if(typeof(item.isDashed)=="undefined") {
								return false;
							}
							return item.isDashed;
						}	
						,changeProperty: {
							caption: 'Dashed line toggle',
							rerender: true
						}
					}
                ]
            ]
        },
	  	{
  		   	caption: 'Arrowheads',
			properties: [
     			[
     			 	{
						caption: 'Filled?'
						,name: 'filledArrow'
						,type: 'onoff'
						,value: function(item,name) {
							if(typeof(item.filledArrow)=="undefined") {
								return false;
							}
							return item.filledArrow;
						}				
						,changeProperty: {
							caption: 'Filled arrowhead line toggle',
							rerender: true
						}
					}
     			]
     		]
  	    },
	  	{
  		   	caption: 'Arrow Direction',
			properties: [
     			[
     			 	{
						caption: false,
						proptype: 'arrow-direction',
						name: 'arrowDirection',
						type: 'radio',
						value: function(item, name) {
							return item.arrowDirection;
						},
						values: [
							{value: 'end',displayValue: '<span class="property-icon property-arrow-dir-end" title="Start"></span>'},
							{value: 'both',displayValue: '<span class="property-icon property-arrow-dir-both" title="Both"></span>'},
							{value: 'front',displayValue: '<span class="property-icon property-arrow-dir-start" title="End"></span>'}
						],
						changeProperty: {
							caption: 'Arrow direction',
							rerender: true
						}
					}
     			]
     		]
  	   }
	]
}

//TYPE = ARROWS
prx.types.arrow1 = prx.componentsHelper.cloneobject(prx.types.arrow);
prx.types.arrow1.name = 'arrow1';

//TYPE = ARROWS WITH ONLY ONE DIRECTION AT ANY TIME
prx.types.arrow2 = prx.componentsHelper.cloneobject(prx.types.arrow);
prx.types.arrow2.name = 'arrow2';
prx.types.arrow2.propertyGroups = prx.componentsHelper.editProperty(prx.types.arrow2.propertyGroups, 'arrowDirection', 'values', [{value: 'end',displayValue: '<span class="property-icon property-arrow-dir-end" title="Start"></span>'},{value: 'front',displayValue: '<span class="property-icon property-arrow-dir-start" title="End"></span>'}]);

//TYPE = CUSTOM ARROWS
prx.types.arrow3 = prx.componentsHelper.cloneobject(prx.types.arrow);
prx.types.arrow3.name = 'arrow3';
prx.types.arrow3.propertyGroups = prx.componentsHelper.editProperty(prx.types.arrow3.propertyGroups, 'arrowDirection', 'values', [{value: 'end',displayValue: '<span class="property-icon property-arrow-dir-end" title="Reverse"></span>'},{value: 'front',displayValue: '<span class="property-icon property-arrow-dir-start" title="Forward"></span>'}]);
prx.componentsHelper.removeProperties(prx.types.arrow3.propertyGroups, ['borderWidth','isDashed', 'filledArrow']);


//TYPE: BUBBLE
prx.types.bubble = {
	name: "bubble"
	,onDisplay: function(item,containerid) {
		
		var _id = (!containerid) ? item.id : containerid+'-'+item.id;
		var dims = prx.componentsHelper.getRealDims(item);
		
		var _props = (jQuery.inArray("bold",item.textProperties)>-1) ? " font-weight: bold;" : "";
		_props += (jQuery.inArray("italic",item.textProperties)>-1) ? " font-style: italic;" : "";
		_props += (jQuery.inArray("underline",item.textProperties)>-1) ? " text-decoration: underline;" : "";
		
		var cwidth, cheight;
		
		if(item.bubbleType == 'rounded' || item.bubbleType == 'rounded-corners') {
			
			if(item.tipDirection == 'left' || item.tipDirection == 'right') {
				cwidth = dims.width - (Math.min(dims.width,dims.height)*0.15) - item.borderWidth*2;
				cheight = dims.height - item.borderWidth*2;
			}
			else if(item.tipDirection == 'top' || item.tipDirection == 'bottom') {
				cwidth = dims.width - item.borderWidth*2;
				cheight = dims.height - (Math.min(dims.width,dims.height)*0.15) - item.borderWidth*2;
			}
		}
		else if(item.bubbleType == 'think-cloud') {
			cwidth = dims.width - item.borderWidth*2;
			cheight = dims.height*0.6 - item.borderWidth*2;			
		}
		else {
			cwidth = dims.width - (Math.min(dims.width,dims.height)*0.15) - item.borderWidth*2;
			cheight = dims.height - (Math.min(dims.width,dims.height)*0.15) - item.borderWidth*2;			
		}

		if(item.bubbleType == 'rounded-corners') {
			item.borderRadius = Math.min(dims.width+(Math.min(dims.width,dims.height)*0.30)-cwidth,dims.height+(Math.min(dims.width,dims.height)*0.30)-cheight)/2;
		}
		else if(item.bubbleType == 'rounded'){
			item.borderRadius = Math.min(dims.width+(Math.min(dims.width,dims.height)*0.30),dims.height+(Math.min(dims.width,dims.height)*0.30))/2;
		}

		var cR = '<div id="' + _id + '" class="box pos type-bubble '+item.bubbleType+'" ';
		cR += 'data-border-width="'+item.borderWidth+'" data-direction-tip="'+item.tipDirection+'">';
		
		cR += '<style>';
		cR += '#'+_id+' .bubble-text-container { width: '+cwidth+'px; height: '+cheight+'px; '+_props+' '+prx.componentsHelper.getFontCssFromFontFamily(item.textFont)+'; font-size: '+item.textSize+'px; text-align: '+item.textAlign+'; color: '+prx.utils.getColor(item.textColor)+'; }';
		if(item.bubbleType == 'rounded' || item.bubbleType == 'rounded-corners') {
			if(item.tipDirection == 'bottom' || item.tipDirection == 'right') {
				cR += '#'+_id+' .bubble-text-container { top: 0; left: 0; }';
			}
			if(item.tipDirection == 'top' || item.tipDirection == 'left') {
				cR += '#'+_id+' .bubble-text-container { bottom: 0; right: 0; }';
			}
		}
		else if(item.bubbleType == 'think-cloud') {
			cR += '#'+_id+' .bubble-text-container { top: 0; left: 0; }';
		}
		else {
			cR += '#'+_id+' .bubble-text-container { top: '+((dims.height-cheight)/2)+'px; left: '+((dims.width-cwidth)/2)+'px; }';
		}
		cR += '</style>';
						
		cR += '<div id="'+item.bubbleType+'-' + _id + '" class="bubble-wrapper">';
		
		cR += '<svg id="svg-'+ _id +'" viewBox="0 0 '+(dims.width)+' '+(dims.height)+'" preserveAspectRatio="none" xmlns="http://www.w3.org/2000/svg" version="1.1">';
						
		var path = prx.componentsHelper.drawBubble( item, item.bubbleType, dims.width, dims.height, item.borderWidth);
				
		cR += '<path d="'+path+'" id="path-'+ _id +'" class="liveUpdate-backgroundColor liveUpdate-borderColor changeProperty-backgroundColor changeProperty-borderColor changeProperty-borderWidth"';
			cR += 'style="fill:'+prx.utils.getColor(item.backgroundColor)+'; ';
				cR += 'stroke:'+prx.utils.getColor(item.borderColor)+'; stroke-width:'+item.borderWidth+'px; stroke-linejoin:'+item.joinType+'; stroke-miterlimit:'+item.borderWidth+';" />';
		
		if( item.bubbleType == 'think-cloud' ) {
			
			if( item.borderWidth > (dims.width*0.03)) item.borderWidth = dims.width*0.03;
			
			if( item.tipDirection == 'right' ) {
				cR += '<ellipse id="ellipse1-'+ _id +'" cx="'+(dims.width*0.73-item.borderWidth/2)+'" cy="'+(dims.height*0.7-item.borderWidth/2)+'" rx="'+(dims.width*0.1-item.borderWidth/2)+'" ry="'+(dims.width*0.06-item.borderWidth/2)+'"';
					cR += 'class="liveUpdate-backgroundColor liveUpdate-borderColor changeProperty-backgroundColor changeProperty-borderColor changeProperty-borderWidth"';
					cR += 'style="fill:'+prx.utils.getColor(item.backgroundColor)+'; ';
						cR += 'stroke:'+prx.utils.getColor(item.borderColor)+'; stroke-width:'+item.borderWidth+'px; stroke-linejoin:'+item.joinType+';" />';
				
				cR += '<ellipse id="ellipse2-'+ _id +'" cx="'+(dims.width*0.8-item.borderWidth/2)+'" cy="'+(dims.height*0.85-item.borderWidth/2)+'" rx="'+(dims.width*0.07-item.borderWidth/2)+'" ry="'+(dims.width*0.04-item.borderWidth/2)+'"';
					cR += 'class="liveUpdate-backgroundColor liveUpdate-borderColor changeProperty-backgroundColor changeProperty-borderColor changeProperty-borderWidth"';
					cR += 'style="fill:'+prx.utils.getColor(item.backgroundColor)+'; ';
						cR += 'stroke:'+prx.utils.getColor(item.borderColor)+'; stroke-width:'+item.borderWidth+'px; stroke-linejoin:'+item.joinType+';" />';
				
				cR += '<ellipse id="ellipse3-'+ _id +'" cx="'+(dims.width*0.85-item.borderWidth/2)+'" cy="'+(dims.height*0.95-item.borderWidth/2)+'" rx="'+(dims.width*0.03-item.borderWidth/2)+'" ry="'+(dims.width*0.03-item.borderWidth/2)+'"';
					cR += 'class="liveUpdate-backgroundColor liveUpdate-borderColor changeProperty-backgroundColor changeProperty-borderColor changeProperty-borderWidth"';
					cR += 'style="fill:'+prx.utils.getColor(item.backgroundColor)+'; ';
						cR += 'stroke:'+prx.utils.getColor(item.borderColor)+'; stroke-width:'+item.borderWidth+'px; stroke-linejoin:'+item.joinType+';" />';
			}
			else if( item.tipDirection == 'left' ) {
				cR += '<ellipse id="ellipse1-'+ _id +'" cx="'+(dims.width*0.27-item.borderWidth/2)+'" cy="'+(dims.height*0.7-item.borderWidth/2)+'" rx="'+(dims.width*0.1-item.borderWidth/2)+'" ry="'+(dims.width*0.06-item.borderWidth/2)+'"';
					cR += 'class="liveUpdate-backgroundColor liveUpdate-borderColor changeProperty-backgroundColor changeProperty-borderColor changeProperty-borderWidth"';
					cR += 'style="fill:'+prx.utils.getColor(item.backgroundColor)+'; ';
						cR += 'stroke:'+prx.utils.getColor(item.borderColor)+'; stroke-width:'+item.borderWidth+'px; stroke-linejoin:'+item.joinType+';" />';
				
				cR += '<ellipse id="ellipse2-'+ _id +'" cx="'+(dims.width*0.2-item.borderWidth/2)+'" cy="'+(dims.height*0.85-item.borderWidth/2)+'" rx="'+(dims.width*0.07-item.borderWidth/2)+'" ry="'+(dims.width*0.04-item.borderWidth/2)+'"';
					cR += 'class="liveUpdate-backgroundColor liveUpdate-borderColor changeProperty-backgroundColor changeProperty-borderColor changeProperty-borderWidth"';
					cR += 'style="fill:'+prx.utils.getColor(item.backgroundColor)+'; ';
						cR += 'stroke:'+prx.utils.getColor(item.borderColor)+'; stroke-width:'+item.borderWidth+'px; stroke-linejoin:'+item.joinType+';" />';
				
				cR += '<ellipse id="ellipse3-'+ _id +'" cx="'+(dims.width*0.15-item.borderWidth/2)+'" cy="'+(dims.height*0.95-item.borderWidth/2)+'" rx="'+(dims.width*0.03-item.borderWidth/2)+'" ry="'+(dims.width*0.03-item.borderWidth/2)+'"';
					cR += 'class="liveUpdate-backgroundColor liveUpdate-borderColor changeProperty-backgroundColor changeProperty-borderColor changeProperty-borderWidth"';
					cR += 'style="fill:'+prx.utils.getColor(item.backgroundColor)+'; ';
						cR += 'stroke:'+prx.utils.getColor(item.borderColor)+'; stroke-width:'+item.borderWidth+'px; stroke-linejoin:'+item.joinType+';" />';
			}
		}
		cR += '</svg>';
		
		cR += '<div class="bubble-text-container liveUpdate-textColor">';
		cR += '<span data-editableproperty="text">' + item.text + '</span>';
		cR += '</div>';
		cR += '</div>';
		cR += '</div>';

		delete item.borderRadius;

		return cR;
	}
	,onResize: function(item,containerid) {

		var _id = (!containerid) ? item.id : containerid+'-'+item.id;
		var dims = prx.componentsHelper.getRealDims(item);
						
		var cwidth, cheight;
		
		if(item.bubbleType == 'rounded' || item.bubbleType == 'rounded-corners') {
			
			if(item.tipDirection == 'left' || item.tipDirection == 'right') {
				cwidth = dims.width - (Math.min(dims.width,dims.height)*0.15) - item.borderWidth*2;
				cheight = dims.height - item.borderWidth*2;
			}
			else if(item.tipDirection == 'top' || item.tipDirection == 'bottom') {
				cwidth = dims.width - item.borderWidth*2;
				cheight = dims.height - (Math.min(dims.width,dims.height)*0.15) - item.borderWidth*2;
			}
					
			$('#'+_id).find('.bubble-text-container').css({
				'width': cwidth + 'px',
				'height': cheight + 'px'
			});
		}

		if(item.bubbleType == 'rounded-corners') {
			item.borderRadius = Math.min(dims.width+(Math.min(dims.width,dims.height)*0.30)-cwidth,dims.height+(Math.min(dims.width,dims.height)*0.30)-cheight)/2;
		}
		else if(item.bubbleType == 'rounded'){
			item.borderRadius = Math.min(dims.width+(Math.min(dims.width,dims.height)*0.30),dims.height+(Math.min(dims.width,dims.height)*0.30))/2;
		}
		
		if(item.bubbleType == 'cloud') {
			cwidth = dims.width - (Math.min(dims.width,dims.height)*0.15) - item.borderWidth*2;
			cheight = dims.height - (Math.min(dims.width,dims.height)*0.15) - item.borderWidth*2;			
			
			$('#'+_id).find('.bubble-text-container').css({
				'top': (dims.height-cheight)/2+'px',
				'left': (dims.width-cwidth)/2+'px'
			});
		}	

		document.getElementById('svg-'+ _id).setAttribute('viewBox', '0 0 '+(dims.width)+' '+(dims.height));
		document.getElementById('path-'+ _id).setAttribute('d', prx.componentsHelper.drawBubble( item, item.bubbleType, dims.width, dims.height, item.borderWidth));

		if( item.bubbleType == 'think-cloud' ) {
			
			if( item.borderWidth > (dims.width*0.03)) item.borderWidth = dims.width*0.03;
			
			if( item.tipDirection == 'right' ) {
				document.getElementById('ellipse1-'+ _id).setAttribute('cx', dims.width*0.73-item.borderWidth/2);	
			
				document.getElementById('ellipse2-'+ _id).setAttribute('cx', dims.width*0.8-item.borderWidth/2);	
			
				document.getElementById('ellipse3-'+ _id).setAttribute('cx', dims.width*0.85-item.borderWidth/2);
			}
			else if( item.tipDirection == 'left' ) {
				document.getElementById('ellipse1-'+ _id).setAttribute('cx', dims.width*0.27-item.borderWidth/2);
				
				document.getElementById('ellipse2-'+ _id).setAttribute('cx', dims.width*0.23-item.borderWidth/2);	
				
				document.getElementById('ellipse3-'+ _id).setAttribute('cx', dims.width*0.15-item.borderWidth/2);
			}		
			
			document.getElementById('ellipse1-'+ _id).setAttribute('cy', dims.height*0.7-item.borderWidth/2);
			document.getElementById('ellipse1-'+ _id).setAttribute('rx', dims.width*0.1-item.borderWidth/2);
			document.getElementById('ellipse1-'+ _id).setAttribute('ry', dims.width*0.06-item.borderWidth/2);
			
			document.getElementById('ellipse2-'+ _id).setAttribute('cy', dims.height*0.85-item.borderWidth/2);
			document.getElementById('ellipse2-'+ _id).setAttribute('rx', dims.width*0.07-item.borderWidth/2);
			document.getElementById('ellipse2-'+ _id).setAttribute('ry', dims.width*0.04-item.borderWidth/2);
			
			document.getElementById('ellipse3-'+ _id).setAttribute('cy', dims.height*0.95-item.borderWidth/2);
			document.getElementById('ellipse3-'+ _id).setAttribute('rx', dims.width*0.03-item.borderWidth/2);
			document.getElementById('ellipse3-'+ _id).setAttribute('ry', dims.width*0.03-item.borderWidth/2);
		}
		
		delete item.borderRadius;

	}
	,editableProperties: [
  		{
  	    	caption: 'Text'
  	    	,name: 'text'
  	    	,type: 'textarea'
  	    	,value: function(item,name) {
  	    		if(typeof(item.text) == "undefined") { item.text = '' }
  	    		return item.text;
  	    	},
    		changeProperty: {
				caption: 'Text',
				property: 'text',
				selector: '.rectangle-text-container span',
				transitionable: false
			}
  	    }
  	]
	,interactions: [
		prx.commonproperties.actions
	]
	,propertyGroups: [	
		{
			caption: 'Text',
			properties: 
			[
				[
					{
						caption: false,
						name: 'textFont',
						proptype: 'font-family',
						type: 'select',
						value: function(item,name) {
							if(typeof(item.textFont) == "undefined") { item.textFont = 'sans-serif,Helvetica Neue,Arial' }
							return item.textFont;
						},
						values: function(){ return prx.comps.fonts }
			      		,changeProperty: {
							caption: ' Text font',
							selector: '.liveUpdate-textColor',
							property: 'font-family',
							transitionable: false
						 }
			
					},
					{
						caption: false,
						name: 'textSize',
						proptype: 'font-size',
						type: 'combo-select',
						value: function(item,name) {
							if(typeof(item.textSize) == "undefined") { item.textSize = 16 }
							return item.textSize;
						},
						values: prx.comps.textsize
			      		,changeProperty: {
							caption: ' Text size',
							selector: '.liveUpdate-textColor',
							property: 'font-size',
							transitionable: false
						 }
					},
			      	{
			      		caption: false,
			      		name: 'textColor',
			      		proptype: 'font-color',
			      		type: 'colorpicker',
			      		value: function(item,name) {
			      			if(typeof(item.textColor) == "undefined") { item.textColor = '#2E2E2E' }
			      			return item.textColor;
			      		},
			      		liveUpdate: 'color'
			      		,changeProperty: {
							caption: ' Text color',
							selector: '.liveUpdate-textColor',
							property: 'color',
							transitionable: true
						 }
			      	}
		      	],
				[
					{
						caption: false,
						name: 'textProperties',
						proptype: 'text-properties',
						type: 'checkbox',
						value: function(item,name) { if(typeof(item.textProperties) == "undefined") {item.textProperties = [];} return item.textProperties; },
						values: [{ value: 'bold', displayValue: '<span class="property-icon property-text-bold" title="Bold"></span>'}, { value: 'italic', displayValue: '<span class="property-icon property-text-italic" title="Italic"></span>'}, { value: 'underline', displayValue: '<span class="property-icon property-text-underline" title="Underline"></span>'}],
			      		changeProperty: {
							caption: 'Text Properties',
							rerender: true
			  			}
					},
			  		{
			  			caption: false,
			  			name: 'textAlign',
			  			proptype: 'text-align',
			  			type: 'radio',
			  			value: function(item,name) {
			  				if(typeof(item.textAlign) == "undefined") { item.textAlign = 'center' }
			  				return item.textAlign;
			  			},
			  			values: [{ value: 'left', displayValue: '<span class="property-icon property-align-left" title="Align left"></span>'}, { value: 'center', displayValue: '<span class="property-icon property-align-center" title="Align center"></span>'}, { value: 'right', displayValue: '<span class="property-icon property-align-right" title="Align right"></span>'}],
			  			changeProperty: {
							caption: 'Text Align',
							selector: '.liveUpdate-textColor',
							property: 'text-align',
							transitionable: false
			  			}
			  		}
		  		]
			]
		},
        {
            caption: 'Style',
            properties: [
                 [
                    {
                        caption: 'Background',
                        name: 'backgroundColor',
                        proptype: 'background-color',
                        type: 'colorpicker',
                        value: function (item, name) {
                            return item.backgroundColor;
                        },
                        liveUpdate: 'fill',
                        changeProperty: {
                            caption: 'Background color',
                            selector: '.changeProperty-backgroundColor',
                            property: 'fill',
                            transitionable: true
                        }
                    }
                ],
                [
	                {
		                caption: 'Border',
		                name: 'borderWidth',
		                proptype: 'border-width',
		                type: 'combo-select',
		                value: function(item,name) {
			                return item.borderWidth;
		                },
		                values: { min: 0, max: 20, step: 1 },
		                onChange: function (item, name) {
							if(item.borderWidth <= 0) { $('#property-roundJoin').hide();}
							else { $('#property-roundJoin').show();}
						},
		                changeProperty: {
			                caption: 'Border Width',
			                rerender: true
		                }
	                },
                    {
	                    caption: false,
	                    name: 'borderColor',
	                    proptype: 'border-color',
	                    type: 'colorpicker',
	                    value: function (item, name) {
	                        return item.borderColor;
	                    },
	                    liveUpdate: 'stroke',
	                    changeProperty: {
	                        caption: 'Border color',
	                        selector: '.changeProperty-borderColor',
	                        property: 'stroke',
	                        transitionable: true
	                    }
	                }
                ],
             	[
	                {
						caption: 'Rounded Border'
						,name: 'roundJoin'
						,type: 'onoff'
						,value: function(item,name) {
							if(typeof(item.roundJoin)=="undefined") {
								return false;
							}
							return item.roundJoin;
						}	
		                ,onChange: function (item, name) {
							if(item.roundJoin) { item.joinType = 'round';}
							else { item.joinType = 'miter';}
						}
		                ,hiddenByDefault: function(item) {
				            return (item.borderWidth <= 0);
			            }
						,changeProperty: {
							caption: 'Rounded border',
							rerender: true
						}
					}
                ]
            ]
        },
	  	{
  		   	caption: 'Tip Direction',
			properties: [
     			[
     			 	{
						caption: false,
						proptype: 'arrow-direction',
						name: 'tipDirection',
						type: 'radio',
						value: function(item, name) {
							return item.tipDirection;
						},
						values: [
							{value: 'bottom',displayValue: '<span class="property-icon property-bubble-tip-bottom" title="Bottom"></span>'},
							{value: 'left',displayValue: '<span class="property-icon property-bubble-tip-left" title="Left"></span>'},
							{value: 'top',displayValue: '<span class="property-icon property-bubble-tip-top" title="Top"></span>'},
							{value: 'right',displayValue: '<span class="property-icon property-bubble-tip-right" title="Right"></span>'},
						],
						changeProperty: {
							caption: 'Bubble Tip direction',
							rerender: true
						}
					}
     			]
     		]
  	   }
	]
}

//TYPE = ARROWS WITH ONLY ONE DIRECTION AT ANY TIME
prx.types.bubble2 = prx.componentsHelper.cloneobject(prx.types.bubble);
prx.types.bubble2.name = 'bubble2';
prx.types.bubble2.propertyGroups = prx.componentsHelper.editProperty(prx.types.bubble2.propertyGroups, 'tipDirection', 'values', [{value: 'left',displayValue: '<span class="property-icon property-bubble-think-tip-left" title="Left"></span>'},{value: 'right',displayValue: '<span class="property-icon property-bubble-think-tip-right" title="Right"></span>'}]);


/************************************* COMPONENTS (OBJECTS) *************************************/

prx.components.arrow1_arrow = {
	name: 'arrow1_arrow'
	,type: 'arrow1'
	,lib: _library
	,caption: 'Arrow'
	,icon: '-160px -8px'
	,width: 150*prx.componentsHelper.getScale(_library)
	,height: 18*prx.componentsHelper.getScale(_library)
	,helper: prx.url.devices+ _path + '/arrow1/helper.png'
	,backgroundColor: '555555'
	,borderColor: '555555'
	,borderWidth: 2*prx.componentsHelper.getScale(_library)
	,arrowType: 'straight'
	,arrowName: 'arrow1'
	,arrowDirection: 'front'
	,joinType: 'round'
	,capType: 'round'
	,isDashed: false
	,filledArrow: false
	,actions:[]
}

prx.components.arrow1_arrow_curved = {
	name: 'arrow1_arrow_curved'
	,type: 'arrow1'
	,lib: _library
	,caption: 'Arrow'
	,icon: '-210px -8px'
	,width: 150*prx.componentsHelper.getScale(_library)
	,height: 25*prx.componentsHelper.getScale(_library)
	,helper: prx.url.devices+ _path + '/arrow1curved/helper.png'
	,backgroundColor: '555555'
	,borderColor: '555555'
	,borderWidth: 2*prx.componentsHelper.getScale(_library)
	,arrowType: 'curved'
	,arrowName: 'arrow1'
	,arrowDirection: 'front'
	,joinType: 'round'
	,capType: 'round'
	,isDashed: false
	,filledArrow: false
	,actions:[]
}

prx.components.arrow1_arrow_arced = {
	name: 'arrow1_arrow_arced'
	,type: 'arrow1'
	,lib: _library
	,caption: 'Arrow'
	,icon: '-260px -8px'
	,width: 50*prx.componentsHelper.getScale(_library)
	,height: 150*prx.componentsHelper.getScale(_library)
	,helper: prx.url.devices+ _path + '/arrow1arced/helper.png'
	,backgroundColor: '555555'
	,borderColor: '555555'
	,borderWidth: 2*prx.componentsHelper.getScale(_library)
	,arrowType: 'arced'
	,arrowName: 'arrow1'
	,arrowDirection: 'front'
	,joinType: 'round'
	,capType: 'round'
	,isDashed: false
	,filledArrow: false
	,actions:[]
}

prx.components.arrow1_arrow_rightangled = {
	name: 'arrow1_arrow_rightangled'
	,type: 'arrow1'
	,lib: _library
	,caption: 'Arrow'
	,icon: '-310px -8px'
	,width: 150*prx.componentsHelper.getScale(_library)
	,height: 50*prx.componentsHelper.getScale(_library)
	,helper: prx.url.devices+ _path + '/arrow1angled/helper.png'
	,backgroundColor: '555555'
	,borderColor: '555555'
	,borderWidth: 2*prx.componentsHelper.getScale(_library)
	,arrowType: 'right-angle'
	,arrowName: 'arrow1'
	,arrowDirection: 'front'
	,joinType: 'round'
	,capType: 'round'
	,isDashed: false
	,filledArrow: false
	,actions:[]
}

prx.components.arrow2_arrow = {
	name: 'arrow2_arrow'
	,type: 'arrow2'
	,lib: _library
	,caption: 'Arrow'
	,icon: '-360px -8px'
	,width: 150*prx.componentsHelper.getScale(_library)
	,height: 18*prx.componentsHelper.getScale(_library)
	,helper: prx.url.devices+ _path + '/arrow2/helper.png'
	,backgroundColor: '555555'
	,borderColor: '555555'
	,borderWidth: 2*prx.componentsHelper.getScale(_library)
	,arrowType: 'straight'
	,arrowName: 'arrow2'
	,arrowDirection: 'front'
	,joinType: 'round'
	,capType: 'round'
	,isDashed: false
	,filledArrow: false
	,actions:[]
}

prx.components.arrow2_arrow_rightangled = {
	name: 'arrow2_arrow_rightangled'
	,type: 'arrow2'
	,lib: _library
	,caption: 'Arrow'
	,icon: '-410px -8px'
	,width: 150*prx.componentsHelper.getScale(_library)
	,height: 50*prx.componentsHelper.getScale(_library)
	,helper: prx.url.devices+ _path + '/arrow2angled/helper.png'
	,backgroundColor: '555555'
	,borderColor: '555555'
	,borderWidth: 2*prx.componentsHelper.getScale(_library)
	,arrowType: 'right-angle'
	,arrowName: 'arrow2'
	,arrowDirection: 'front'
	,joinType: 'round'
	,capType: 'round'
	,isDashed: false
	,filledArrow: false
	,actions:[]
}

prx.components.arrow3_arrow = {
	name: 'arrow3_arrow'
	,type: 'arrow2'
	,lib: _library
	,caption: 'Arrow'
	,icon: '-810px -8px'
	,width: 150*prx.componentsHelper.getScale(_library)
	,height: 18*prx.componentsHelper.getScale(_library)
	,helper: prx.url.devices+ _path + '/arrow3/helper.png'
	,backgroundColor: '555555'
	,borderColor: '555555'
	,borderWidth: 2*prx.componentsHelper.getScale(_library)
	,arrowType: 'straight'
	,arrowName: 'arrow3'
	,arrowDirection: 'front'
	,joinType: 'round'
	,capType: 'round'
	,isDashed: false
	,filledArrow: false
	,actions:[]
}

prx.components.arrow3_arrow_rightangled = {
	name: 'arrow3_arrow_rightangled'
	,type: 'arrow2'
	,lib: _library
	,caption: 'Arrow'
	,icon: '-760px -8px'
	,width: 150*prx.componentsHelper.getScale(_library)
	,height: 50*prx.componentsHelper.getScale(_library)
	,helper: prx.url.devices+ _path + '/arrow3angled/helper.png'
	,backgroundColor: '555555'
	,borderColor: '555555'
	,borderWidth: 2*prx.componentsHelper.getScale(_library)
	,arrowType: 'right-angle'
	,arrowName: 'arrow3'
	,arrowDirection: 'front'
	,joinType: 'round'
	,capType: 'round'
	,isDashed: false
	,filledArrow: false
	,actions:[]
}

prx.components.arrow4_arrow = {
	name: 'arrow4_arrow'
	,type: 'arrow1'
	,lib: _library
	,caption: 'Arrow'
	,icon: '-710px -8px'
	,width: 150*prx.componentsHelper.getScale(_library)
	,height: 18*prx.componentsHelper.getScale(_library)
	,helper: prx.url.devices+ _path + '/arrow4/helper.png'
	,backgroundColor: '555555'
	,borderColor: '555555'
	,borderWidth: 2*prx.componentsHelper.getScale(_library)
	,arrowType: 'straight'
	,arrowName: 'arrow4'
	,arrowDirection: 'front'
	,joinType: 'round'
	,capType: 'round'
	,isDashed: false
	,filledArrow: false
	,actions:[]
}

prx.components.arrow5_arrow = {
	name: 'arrow5_arrow'
	,type: 'arrow3'
	,lib: _library
	,caption: 'Arrow'
	,icon: '-660px -8px'
	,width: 75*prx.componentsHelper.getScale(_library)
	,height: 50*prx.componentsHelper.getScale(_library)
	,helper: prx.url.devices+ _path + '/arrow5/helper.png'
	,backgroundColor: '555555'
	,borderColor: '555555'
	,borderWidth: 2*prx.componentsHelper.getScale(_library)
	,arrowType: 'custom'
	,arrowName: 'arrow5'
	,arrowDirection: 'front'
	,joinType: 'round'
	,capType: 'round'
	,isDashed: false
	,filledArrow: false
	,actions:[]
}

prx.components.arrow6_arrow = {
	name: 'arrow6_arrow'
	,type: 'arrow3'
	,lib: _library
	,caption: 'Arrow'
	,icon: '-610px -8px'
	,width: 50*prx.componentsHelper.getScale(_library)
	,height: 50*prx.componentsHelper.getScale(_library)
	,helper: prx.url.devices+ _path + '/arrow6/helper.png'
	,backgroundColor: '555555'
	,borderColor: '555555'
	,borderWidth: 2*prx.componentsHelper.getScale(_library)
	,arrowType: 'custom'
	,arrowName: 'arrow6'
	,arrowDirection: 'front'
	,joinType: 'round'
	,capType: 'round'
	,isDashed: false
	,filledArrow: false
	,actions:[]
}

prx.components.arrow7_arrow = {
	name: 'arrow7_arrow'
	,type: 'arrow3'
	,lib: _library
	,caption: 'Arrow'
	,icon: '-560px -8px'
	,width: 50*prx.componentsHelper.getScale(_library)
	,height: 50*prx.componentsHelper.getScale(_library)
	,helper: prx.url.devices+ _path + '/arrow7/helper.png'
	,backgroundColor: '555555'
	,borderColor: '555555'
	,borderWidth: 2*prx.componentsHelper.getScale(_library)
	,arrowType: 'custom'
	,arrowName: 'arrow7'
	,arrowDirection: 'front'
	,joinType: 'round'
	,capType: 'round'
	,isDashed: false
	,filledArrow: false
	,actions:[]
}

prx.components.arrow8_arrow = {
	name: 'arrow8_arrow'
	,type: 'arrow3'
	,lib: _library
	,caption: 'Arrow'
	,icon: '-460px -8px'
	,width: 90*prx.componentsHelper.getScale(_library)
	,height: 100*prx.componentsHelper.getScale(_library)
	,helper: prx.url.devices+ _path + '/arrow8/helper.png'
	,backgroundColor: '555555'
	,borderColor: '555555'
	,borderWidth: 2*prx.componentsHelper.getScale(_library)
	,arrowType: 'custom'
	,arrowName: 'arrow8'
	,arrowDirection: 'front'
	,joinType: 'round'
	,capType: 'round'
	,isDashed: false
	,filledArrow: false
	,actions:[]
}

prx.components.bubble = {
	name: 'bubble'
	,type: 'bubble'
	,lib: _library
	,caption: 'Speech Bubble'
	,icon: '-60px -8px'
	,width: 150*prx.componentsHelper.getScale(_library)
	,height: 100*prx.componentsHelper.getScale(_library)
	,helper: prx.url.devices+ _path + '/bubble1/helper.png'
	,backgroundColor: 'C6C6C6'
	,borderColor: '555555'
	,borderWidth: 0*prx.componentsHelper.getScale(_library)
	,borderRadius: 0*prx.componentsHelper.getScale(_library)
	,textFont: 'sans-serif,Helvetica Neue,Arial'
	,textSize: 16*prx.componentsHelper.getScale(_library)
	,textColor:  '#383838'
	,textProperties: []
	,textAlign: 'center'
	,text: ''
	,bubbleType: 'rounded-corners'
	,tipDirection: 'bottom'
	,joinType: 'round'
	,roundJoin: true
	,actions:[]
}

prx.components.bubble_round = {
	name: 'bubble_round'
	,type: 'bubble'
	,lib: _library
	,caption: 'Speech Bubble'
	,icon: '-110px -8px'
	,width: 150*prx.componentsHelper.getScale(_library)
	,height: 100*prx.componentsHelper.getScale(_library)
	,helper: prx.url.devices+ _path + '/bubble2/helper.png'
	,backgroundColor: 'C6C6C6'
	,borderColor: '555555'
	,borderWidth: 0*prx.componentsHelper.getScale(_library)
	,borderRadius: 0*prx.componentsHelper.getScale(_library)
	,textFont: 'sans-serif,Helvetica Neue,Arial'
	,textSize: 16*prx.componentsHelper.getScale(_library)
	,textColor:  '#383838'
	,textProperties: []
	,textAlign: 'center'
	,text: ''
	,bubbleType: 'rounded'
	,tipDirection: 'bottom'
	,joinType: 'round'
	,roundJoin: true
	,actions:[]
}

prx.components.bubble_cloud = {
	name: 'bubble_cloud'
	,type: 'bubble'
	,lib: _library
	,caption: 'Speech Bubble'
	,icon: '-10px -8px'
	,width: 150*prx.componentsHelper.getScale(_library)
	,height: 100*prx.componentsHelper.getScale(_library)
	,helper: prx.url.devices+ _path + '/bubble3/helper.png'
	,backgroundColor: 'C6C6C6'
	,borderColor: '555555'
	,borderWidth: 0*prx.componentsHelper.getScale(_library)
	,borderRadius: 0*prx.componentsHelper.getScale(_library)
	,textFont: 'sans-serif,Helvetica Neue,Arial'
	,textSize: 16*prx.componentsHelper.getScale(_library)
	,textColor:  '#383838'
	,textProperties: []
	,textAlign: 'center'
	,text: ''
	,bubbleType: 'cloud'
	,tipDirection: 'bottom'
	,joinType: 'round'
	,roundJoin: true
	,actions:[]
}

prx.components.bubble_think_cloud = {
	name: 'bubble_think_cloud'
	,type: 'bubble2'
	,lib: _library
	,caption: 'Speech Bubble'
	,icon: '-860px -8px'
	,width: 150*prx.componentsHelper.getScale(_library)
	,height: 125*prx.componentsHelper.getScale(_library)
	,helper: prx.url.devices+ _path + '/bubble4/helper.png'
	,backgroundColor: 'C6C6C6'
	,borderColor: '555555'
	,borderWidth: 0*prx.componentsHelper.getScale(_library)
	,borderRadius: 0*prx.componentsHelper.getScale(_library)
	,textFont: 'sans-serif,Helvetica Neue,Arial'
	,textSize: 16*prx.componentsHelper.getScale(_library)
	,textColor:  '#383838'
	,textProperties: []
	,textAlign: 'center'
	,text: ''
	,bubbleType: 'think-cloud'
	,tipDirection: 'left'
	,joinType: 'round'
	,roundJoin: true
	,actions:[]
}



/** DUMMIES **/
prx.components.arrow = {
	name: 'arrow'
	,type: 'arrow'
	,lib: _library
	,caption: 'Arrow'
	,icon: '-160px 0'
	,width: 150*prx.componentsHelper.getScale(_library)
	,height: 18*prx.componentsHelper.getScale(_library)
	,helper: prx.url.devices+ _path + '/dummy/helper.png'
	,backgroundColor: '555555'
	,borderColor: '555555'
	,borderWidth: 2*prx.componentsHelper.getScale(_library)
	,arrowType: 'straight'
	,arrowName: 'arrow1'
	,arrowDirection: 'front'
	,joinType: 'round'
	,capType: 'round'
	,isDashed: false
	,filledArrow: false
	,actions:[]
}

prx.components.arrow1 = {
	name: 'arrow1'
	,type: 'arrow1'
	,lib: _library
	,caption: 'Arrow'
	,icon: '-160px 0'
	,width: 150*prx.componentsHelper.getScale(_library)
	,height: 18*prx.componentsHelper.getScale(_library)
	,helper: prx.url.devices+ _path + '/dummy/helper.png'
	,backgroundColor: '555555'
	,borderColor: '555555'
	,borderWidth: 2*prx.componentsHelper.getScale(_library)
	,arrowType: 'straight'
	,arrowName: 'arrow1'
	,arrowDirection: 'front'
	,joinType: 'round'
	,capType: 'round'
	,isDashed: false
	,filledArrow: false
	,actions:[]
}

prx.components.arrow2 = {
	name: 'arrow2'
	,type: 'arrow2'
	,lib: _library
	,caption: 'Arrow'
	,icon: '-160px 0'
	,width: 150*prx.componentsHelper.getScale(_library)
	,height: 18*prx.componentsHelper.getScale(_library)
	,helper: prx.url.devices+ _path + '/dummy/helper.png'
	,backgroundColor: '555555'
	,borderColor: '555555'
	,borderWidth: 2*prx.componentsHelper.getScale(_library)
	,arrowType: 'straight'
	,arrowName: 'arrow2'
	,arrowDirection: 'front'
	,joinType: 'round'
	,capType: 'round'
	,isDashed: false
	,filledArrow: false
	,actions:[]
}

prx.components.arrow3 = {
	name: 'arrow3'
	,type: 'arrow3'
	,lib: _library
	,caption: 'Arrow'
	,icon: '-160px 0'
	,width: 50*prx.componentsHelper.getScale(_library)
	,height: 25*prx.componentsHelper.getScale(_library)
	,helper: prx.url.devices+ _path + '/dummy/helper.png'
	,backgroundColor: '555555'
	,borderColor: '555555'
	,borderWidth: 2*prx.componentsHelper.getScale(_library)
	,arrowType: 'custom'
	,arrowName: 'arrow5'
	,arrowDirection: 'front'
	,joinType: 'round'
	,capType: 'round'
	,isDashed: false
	,filledArrow: false
	,actions:[]
}

prx.components.bubble2 = {
	name: 'bubble2'
	,type: 'bubble2'
	,lib: _library
	,caption: 'Speech Bubble'
	,icon: '-160px 0'
	,width: 90*prx.componentsHelper.getScale(_library)
	,height: 125*prx.componentsHelper.getScale(_library)
	,helper: prx.url.devices+ _path + '/dummy/helper.png'
	,backgroundColor: 'C6C6C6'
	,borderColor: '555555'
	,borderWidth: 0*prx.componentsHelper.getScale(_library)
	,borderRadius: 0*prx.componentsHelper.getScale(_library)
	,textFont: 'sans-serif,Helvetica Neue,Arial'
	,textSize: 16*prx.componentsHelper.getScale(_library)
	,textColor:  '#383838'
	,textProperties: []
	,textAlign: 'center'
	,text: ''
	,bubbleType: 'think-cloud'
	,tipDirection: 'right'
	,joinType: 'round'
	,roundJoin: true
	,actions:[]
}