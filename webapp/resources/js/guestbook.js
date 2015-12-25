function validateEmail(email){
	var pattern = '^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@'
		+ '[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$';
	var re = new RegExp(pattern);
	
	return re.test(email);
}

var newGuestbookView = new Component('#newGuestbookView', {
	isVisible : function isVisible(){
		return this.$root.is(':visible');
	},
	show : function show(){
		this.$root.slideDown(200);
	},
	hide : function hide(){
		this.$root.slideUp(200);
	},
	toggle : function toggle(){
		this.$root.slideToggle({
			direction:'up',
			duration:200
		});
	}},
	
	[['focus', '.inp-email', function(event, $this){
		$this.removeClass('invalid');
	}],
	['focus', '.inp-email', function(event, $this){
		$this.removeClass('invalid');
	}],
	['keyup', '.inp-email', function(event, $this){
		var isEmailValid = validateEmail($this.val());
		if(isEmailValid){
			$this.removeClass('invalid');
		} else{
			$this.addClass('invalid');
		}
		
		this.$root.find('.btn-submit').prop('disabled', !isEmailValid);
	}],
	['click', '.btn-cancel', function(event, $this){
		event.preventDefault();
		this.$root.slideUp(200);
	}],
	['submit', 'form', function(event, $this){
		var $inpPassword = $this.find('.inp-password');
		var encrypted = CryptoJS.SHA256($inpPassword.val()).toString();
		$inpPassword.val(encrypted);
	}]]);


var modifyGuestbookView = new Component('#modifyGuestbookView', {
	init : function init($root){
		$root.detach();
	},
	isVisible : function isVisible(){
		return this.$root.is(':visible');
	},
	show : function show(){
		this.$root.slideDown(200);
	},

	hideAndDetach : function hideAndDetach(){
		this.$root.hide().detach();
	},
	hide : function hide(){
		this.$root.slideUp(200, function(){
			$(this).detach();
		});
	},
	fill : function fill(data){
		var uri = this.$root.find('form').attr('action');
		uri = uri.slice(0, uri.lastIndexOf('/')+1) +  data.post_id;
		this.$root.find('form').attr('action', uri);
		this.$root.find('.inp-id').val(data.post_id);
		this.$root.find('.inp-email').val(data.email);
		this.$root.find('.inp-body').val(data.body);
		
		this.originalTargetPostBody = data.body;
	},
	clear : function clear(){
		this.$root.find('.inp-email').val(null);
		this.$root.find('.inp-body').val(null);
	},
	restoreTarget : function restoreTarget(){
		if(!this.$targetPost) return;
		
		this.$targetPost.find('.body').text(this.originalTargetPostBody);
	},
	lockPost : function lockPost($post){
		this.$targetPost = $post;
		this.$targetPost.addClass('locked');
	},
	releasePost : function releasePost(){
		if(!this.$targetPost) return;
		
		this.$targetPost.removeClass('locked');
		this.$targetPost = null;
	}},
	
	[['click', '.btn-cancel', function(event, $this){
		event.preventDefault();
		this.hide();
		this.restoreTarget();
		this.releasePost();
	}],
	['keydown', '.inp-body', function(event, $this){
		this.$targetPost.find('.body').text($this.val());
	}],
	['keyup', '.inp-body', function(event, $this){
		this.$targetPost.find('.body').text($this.val());
	}],
	['submit', 'form', function(event, $this){
		var $inpPassword = $this.find('.inp-password');
		var encrypted = CryptoJS.SHA256($inpPassword.val()).toString();
		$inpPassword.val(encrypted);
	}]]);
modifyGuestbookView.members = {
		$targetPost : null,
		originalTargetPostBody :null
};



var guestbookList = new Component('#guestbookList',
	{},
	[['click', '.btn-modify', function(event, $this){
		var $post = $this.parent();
		modifyGuestbookView.clear();
		modifyGuestbookView.restoreTarget();
		modifyGuestbookView.releasePost();
		
		if(($post.next().get(0) !== modifyGuestbookView.$root.get(0)) || !modifyGuestbookView.$root.is(':visible')){
			// append modify guestbook view
			modifyGuestbookView.hideAndDetach();
			$post.after(modifyGuestbookView.$root);		
			modifyGuestbookView.show();
			
			modifyGuestbookView.fill({
				post_id : $post.attr('post_id'),
				email : $post.find('.email').text(),
				body : $post.find('.body').text()
			});
			
			modifyGuestbookView.lockPost($post);
			
			newGuestbookView.hide();
		} else{
			modifyGuestbookView.hide();
		}
	}]]);

var guestbook = new Component($(document),
	{},
	[['click', '#toggleNewGuestbookView', function(event, $this){
		modifyGuestbookView.hide();
		modifyGuestbookView.clear();
		modifyGuestbookView.restoreTarget();
		modifyGuestbookView.releasePost();
		
		newGuestbookView.toggle();
	}]]);


guestbook.attachChild(newGuestbookView);
guestbook.attachChild(modifyGuestbookView);
guestbook.attachChild(guestbookList);

guestbook.init();