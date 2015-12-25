/////////////////////////////////////////////////////////////////////
/*
	Usage)
	 
	var myComponent = new Component('#myComponent', {
		//methods
		init : function init($root){}, //special method called when initialized
		myMethod1 : function myMethod1(){},
		myMethod2 : function myMethod2(){},
		},
		
		[
		//event handlers
		['click', '.btn-test', function(event, $this){
			//this -> this component
			//$this -> $(event.currentTarget)
			console.log(this, event, $this);
		}],
		['keydown', '.inp-test', function(event, $this){
			console.log($this.val());
		}]
		]);
	myComponent.members = {
			member1 : 'member registered when this component initialized',
			member2 :'to use member reference like myComponent.member2'
	};
 */
/////////////////////////////////////////////////////////////////////

var Component = function Component($rootOrRootPath, methods, handlers){
	this.$root = null;
	this.children = [];
	this.members = {};
	
	this.handlerProxy = function(event){		
		var handler = event.data[0];
		var self = event.data[1];
		return handler.apply(self, [event, $(event.currentTarget)]);
	};
	
	var $root = null,
		rootPath = null;
	if(typeof($rootOrRootPath) === 'string'){
		rootPath = $rootOrRootPath;
	} else{
		$root = $rootOrRootPath;
	}
	
	for(var key in methods){
		var method = methods[key];
		
		if(typeof method !== 'function') continue;

		if(key === 'init'){
			this._init = method;
		} else{
			this[key] = method;			
		}
	}
	
	var bInitAlready = false;
	this.init = function(){
		if(bInitAlready) throw "already initialized!";
		
		this.$root = $root || this.parent.$root.find(rootPath).eq(0);
		
		if(this._init) this._init(this.$root);
		
		// bind event handlers
		for(var i=0; i < handlers.length; ++i){
			var handler = handlers[i];
			
			var type = null;
			var delegate = null;
			var fn = null;
			if(handler.length === 2){
				type = handler[0];
				fn = handler[1];
			} else if(handler.length === 3){
				type = handler[0];
				delegate = handler[1];
				fn = handler[2];
			}
			
			var data = [fn, this];
			
			if(delegate){
				this.$root.on(type, delegate, data, this.handlerProxy);
			} else{
				this.$root.on(type, data, this.handlerProxy);
			}
		}
		
		//apply member variables
		for(var key in this.members){
			this[key] = this.members[key];
		}
		
		this.members = null;
		delete this.members; 
		
		for(var i=0; i < this.children.length; ++i){
			this.children[i].init();
		}
		
		bInitAlready = true;
	};
};
Component.prototype.attachChild = function addChild(component){
	component.parent = this;
	this.children.push(component);
};
Component.prototype.detachChild = function detachChild(component){
	for(var i=0; i < thischildren.length; ++i){
		if(this.children[i] === component){
			this.children.splice(i, 1);
			component.parent = null;
			break;
		}
	}
};