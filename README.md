GameboyJam

GAME BOY JAM 11/1-11/10

} else {

$pluginsURI = plugins_url('/easy-social-icons/');
function cnss_my_script() {
    global $pluginsURI;
    wp_enqueue_script( 'jquery' );
    wp_enqueue_script('jquery-ui-sortable');
    wp_register_script('cnss_js', plugins_url( 'js/cnss.js' , __FILE__ ), array(), '1.0' );
    wp_enqueue_script( 'cnss_js' );

    wp_register_style('cnss_css', plugins_url( 'css/cnss.css' , __FILE__ ), array(), '1.0' );
    wp_enqueue_style( 'cnss_css' );
}
add_action('init', 'cnss_my_script');
add_shortcode("disclosures", "disclosure_list");
add_action("init", "mail_disclosures");
add_action('wp_ajax_update-social-icon-order', 'cnss_save_ajax_order' );
add_action('wp_ajax_delete-social-icon', 'cnss_delete_social_icon' );
add_action('admin_menu', 'cnss_add_menu_pages');

function cnss_add_menu_pages() {
    add_submenu_page('realscout_admin', 'Manage Disclosures', 'Manage Disclosures', 'manage_options', 'disclosure_admin', 'disclosure_admin');

    add_submenu_page('realscout_admin', 'Manage Social Icons', 'Manage Social Icons', 'manage_options', 'cnss_social_icon_page', 'cnss_social_icon_page_fn');

    add_submenu_page('realscout_admin', 'Add Social Icons', 'Add Social Icons', 'manage_options', 'cnss_social_icon_add', 'cnss_social_icon_add_fn');

    add_submenu_page('realscout_admin', 'Sort Social Icons', 'Sort Social Icons', 'manage_options', 'cnss_social_icon_sort', 'cnss_social_icon_sort_fn');

    if(current_user_can("edit_themes")) {
        add_submenu_page('realscout_admin', 'Social Options', 'Social Options', 'manage_options', 'cnss_social_icon_option', 'cnss_social_icon_option_fn');
    }

    add_action( 'admin_init', 'register_cnss_settings' );

}

function disclosure_admin() {
    include("../GitHub/RealScout-Themes/realscout_common/disclosures-admin.php");
}

function register_cnss_settings() {
    register_setting( 'cnss-settings-group', 'cnss-width' );
    register_setting( 'cnss-settings-group', 'cnss-height' );
    register_setting( 'cnss-settings-group', 'cnss-margin' );
    register_setting( 'cnss-settings-group', 'cnss-row-count' );
    register_setting( 'cnss-settings-group', 'cnss-max-social' );
    register_setting( 'cnss-settings-group', 'cnss-vertical-horizontal' );
}

function cnss_social_icon_option_fn() {

    $cnss_width = get_option('cnss-width');
    $cnss_height = get_option('cnss-height');
    $cnss_margin = get_option('cnss-margin');
    $cnss_rows = get_option('cnss-row-count');
    $max_social = get_option('cnss-max-social');
    $vorh = get_option('cnss-vertical-horizontal');
    $vertical ='';
    $horizontal ='';
    if($vorh=='vertical') $vertical = 'checked="checked"';
    if($vorh=='horizontal') $horizontal = 'checked="checked"';
    ?>
    <div class="wrap">
        <h2>Social Icon Options</h2>
        <form method="post" action="options.php">
            <?php settings_fields( 'cnss-settings-group' ); ?>
            <table class="form-table">
                <tr valign="top">
                    <th scope="row">Icon Width</th>
                    <td><input type="text" name="cnss-width" id="cnss-width" class="small-text" value="<?php echo $cnss_width?>" />px</td>
                </tr>
                <tr valign="top">
                    <th scope="row">Icon Height</th>
                    <td><input type="text" name="cnss-height" id="cnss-height" class="small-text" value="<?php echo $cnss_height?>" />px</td>
                </tr>
                <tr valign="top">
                    <th scope="row">Icon Margin <em><small>(Gap between each icon)</small></em></th>
                    <td><input type="text" name="cnss-margin" id="cnss-margin" class="small-text" value="<?php echo $cnss_margin?>" />px</td>
                </tr>

                <tr valign="top">
                    <th scope="row">Number of Rows</th>
                    <td><input type="text" name="cnss-row-count" id="cnss-row-count" class="small-text" value="<?php echo $cnss_rows?>" /></td>
                </tr>

                <tr valign="top">
                    <th scope="row">Number of Icons in Nav Bar</th>
                    <td><input type="text" name="cnss-max-social" id="cnss-max-social" class="small-text" value="<?php echo $max_social ?>" /></td>
                </tr>

                <tr valign="top">
                    <th scope="row">Display Icon</th>
                    <td>
                        <input <?php echo $horizontal ?> type="radio" name="cnss-vertical-horizontal" id="horizontal" value="horizontal" />&nbsp;<label for="horizontal">Horizontally</label><br />
                        <input <?php echo $vertical ?> type="radio" name="cnss-vertical-horizontal" id="vertical" value="vertical" />&nbsp;<label for="vertical">Vertically</label></td>
                </tr>
            </table>

            <p class="submit">
                <input type="submit" class="button-primary" value="<?php _e('Save Changes') ?>" />
            </p>
        </form>
    </div>
<?php
}


function cnss_db_install () {
    global $wpdb;
    global $cnss_db_version;

    $srcdir   = ABSPATH.'wp-content/plugins/easy-social-icons/images/icon/';
    $upload_dir = wp_upload_dir();
    $targetdir = $upload_dir['basedir'].'/';

    $files = scandir($srcdir);
    foreach($files as $fname)
    {
        if($fname=='.')continue;
        if($fname=='..')continue;
        copy($srcdir.$fname, $targetdir.$fname);
    }

    $table_name = $wpdb->prefix . "cn_social_icon";
    if($wpdb->get_var("show tables like '$table_name'") != $table_name) {

        $sql2 = "CREATE TABLE " . $table_name . " (
	`id` BIGINT(20) NOT NULL AUTO_INCREMENT, 
	`title` VARCHAR(255) NULL, 
	`url` VARCHAR(255) NOT NULL, 
	`image_url` VARCHAR(255) NOT NULL, 
	`sortorder` INT NOT NULL DEFAULT '0', 
	`date_upload` VARCHAR(100) NULL, 
	`target` tinyint(1) NOT NULL DEFAULT '1',
	PRIMARY KEY (`id`)) ENGINE = InnoDB;
	INSERT INTO `wp_cn_social_icon` (`id`, `title`, `url`, `image_url`, `sortorder`, `date_upload`, `target`) VALUES
	(1, 'facebook', 'http://facebook.com/your-fan-page', '1368459524_facebook.png', 1, '1368459524', 1),
	(2, 'twitter', 'http://twitter/username', '1368459556_twitter.png', 2, '1368459556', 1),
	(3, 'flickr', 'http://flickr.com/?username', '1368459641_flicker.png', 3, '1368459641', 1),
	(4, 'linkedin', 'http://linkedin.com', '1368459699_in.png', 4, '1368459699', 1),
	(5, 'youtube', 'http://youtube.com/user', '1368459724_youtube.png', 5, '1368459724', 1);	
	";
        require_once(ABSPATH . 'wp-admin/includes/upgrade.php');
        dbDelta($sql2);

        add_option( 'cnss-width', '32');
        add_option( 'cnss-height', '32');
        add_option( 'cnss-margin', '4');
        add_option( 'cnss-row-count', '1');
        add_option( 'cnss-max-social', '0');
        add_option( 'cnss-vertical-horizontal', 'horizontal');

    }
}

register_activation_hook(__FILE__,'cnss_db_install');

if (isset($_GET['delete'])) {

    if ($_REQUEST['id'] != '')
    {

        $table_name = $wpdb->prefix . "cn_social_icon";
        $image_file_path = "../wp-content/uploads/";
        $sql = "SELECT * FROM ".$table_name." WHERE id =".$_REQUEST['id'];
        $video_info = $wpdb->get_results($sql);

        if (!empty($video_info) && file_exists($image_file_path.$video_info[0]->image_url))
        {
            @unlink($image_file_path.$video_info[0]->image_url);
        }
        $delete = "DELETE FROM ".$table_name." WHERE id = ".$_REQUEST['id']." LIMIT 1";
        $results = $wpdb->query( $delete );
        $msg = "Delete Successfully!!!"."<br />";
    }

}


if (isset($_POST['submit_button'])) {

    if ($_POST['action'] == 'update')
    {

        $err = "";
        $msg = "";

        $image_file_path = "../wp-content/uploads/";

        if ($_FILES["image_file"]["name"] != "" ){

            if(
                ($_FILES["image_file"]["type"] == "image/gif")
                || ($_FILES["image_file"]["type"] == "image/jpeg")
                || ($_FILES["image_file"]["type"] == "image/pjpeg")
                || ($_FILES["image_file"]["type"] == "image/png")
                && ($_FILES["image_file"]["size"] < 1024*1024*1))
            {
                if ($_FILES["image_file"]["error"] > 0)
                {
                    $err .= "Return Code: " . $_FILES["image_file"]["error"] . "<br />";
                }
                else
                {
                    //if (file_exists($image_file_path . $_FILES["image_file"]["name"]))
                    //{
                    //    $err .= $_FILES["image_file"]["name"] . " already exists. ";
                    //}
                    //else
                    //{
                        $image_file_name = time().'_'.$_FILES["image_file"]["name"];
                        $fstatus = move_uploaded_file($_FILES["image_file"]["tmp_name"], $image_file_path . $image_file_name);
                        if ($fstatus == true){
                            $msg = "File Uploaded Successfully!!!"."<br />";
                        }
                    //}
                }
            }
            else
            {
                $err .= "Invalid file type or max file size exceded" . "<br />";
            }
        }
        elseif($_POST['image_url'] != "") {
            $image_file_name = $_POST['image_url'];
        }
        else {
            $err .= "Please input image file". "<br />";
        }// end if image file

        if ($err == '')
        {
            $table_name = $wpdb->prefix . "cn_social_icon";

            $insert = "INSERT INTO " . $table_name .
                " (title, url, image_url, sortorder, date_upload, target) " .
                "VALUES ('" .
                $wpdb->escape( $_REQUEST['title']) . "','" .
                $wpdb->escape( $_REQUEST['url']) . "','" .
                $image_file_name . "'," .
                $_REQUEST['sortorder'] . ",'" .
                time() . "'," .
                $_REQUEST['target'] . "" .
                ")";
            $results = $wpdb->query( $insert );

            if (!$results) {
                cnss_db_install();
                $results = $wpdb->query( $insert );

                if (!$results)
                    $err .= "Fail to update database" . "<br />";
                else
                    $msg .= "Update Successfull!!!" . "<br />";
            }
            else
                $msg .= "Update Successfull!!!" . "<br />";

        }
    }// end if update

    if ( $_REQUEST['action'] == 'edit' and $_REQUEST['id'] != '' )
    {
        $err = "";
        $msg = "";

        $url = $_REQUEST['url'];
        $target = $_REQUEST['target'];

        $image_file_path = "../wp-content/uploads/";
        $table_name = $wpdb->prefix . "cn_social_icon";
        $sql = "SELECT * FROM ".$table_name." WHERE id =".$_REQUEST['id'];
        $video_info = $wpdb->get_results($sql);
        $image_file_name = $video_info[0]->image_url;
        $update = "";

        $imgExtArray = array('image/gif','image/jpeg','image/pjpeg','image/png');
        $type= 1;
        if ($_FILES["image_file"]["name"] != ""){
            if( in_array($_FILES["image_file"]["type"],$imgExtArray) && $_FILES["image_file"]["size"] <= 1024*1024*1 )
            {
                if ($_FILES["image_file"]["error"] > 0)
                {
                    $err .= "Return Code: " . $_FILES["image_file"]["error"] . "<br />";
                }
                else
                {
                    if (file_exists($image_file_path . $_FILES["image_file"]["name"]))
                    {
                        $err .= $_FILES["image_file"]["name"] . " already exists. ";
                    }
                    else
                    {
                        $image_file_name = time().'_'.$_FILES["image_file"]["name"];
                        $fstatus = move_uploaded_file($_FILES["image_file"]["tmp_name"], $image_file_path . $image_file_name);

                        if ($fstatus == true){
                            $msg = "File Uploaded Successfully!!!".'<br />';
                            @unlink($image_file_path.$video_info[0]->image_url);
                            $update = "UPDATE " . $table_name . " SET " .
                                "image_url='" .$image_file_name . "'" .
                                " WHERE id=" . $_REQUEST['id'];
                            $results1 = $wpdb->query( $update );
                        }
                    }
                }
            }
            else
            {
                $err .= "Invalid file type or max file size exceded";
            }
        }
        elseif($_POST['image_url'] != "") {
            $msg = "File Uploaded Successfully!!!".'<br />';
            @unlink($image_file_path.$video_info[0]->image_url);

            $image_file_name = $_POST['image_url'];
            $update = "UPDATE " . $table_name . " SET " .
                "image_url='" .$image_file_name . "'" .
                " WHERE id=" . $_REQUEST['id'];
            $results1 = $wpdb->query( $update );
        }

        $update = "UPDATE " . $table_name . " SET " .
            "title='" .$wpdb->escape( $_POST['title']) . "'," .
            "url='" . $url . "'," .
            "sortorder=" .$_POST['sortorder'] . "," .
            "date_upload='" .time(). "'," .
            "target=$target " .
            " WHERE id=" . $_POST['id'];
        if ($err == '')
        {
            $table_name = $wpdb->prefix . "cn_social_icon";
            $results3 = $wpdb->query( $update );

            if (!$results3){
                $err .= "Update Fail!!!". "<br />";
            }
            else
            {
                $msg = "Update Successfull!!!". "<br />";
            }
        }

    } // end edit

}


function cnss_social_icon_sort_fn() {
    global $wpdb;

    $cnss_width = get_option('cnss-width');
    $cnss_height = get_option('cnss-height');

    $image_file_path = "../wp-content/uploads/";
    $table_name = $wpdb->prefix . "cn_social_icon";
    $sql = "SELECT * FROM ".$table_name." WHERE 1 ORDER BY sortorder";
    $video_info = $wpdb->get_results($sql);

    ?>
    <div class="wrap">
        <h2>Sort Icon</h2>

        <div id="ajax-response"></div>

        <noscript>
            <div class="error message">
                <p><?php _e('This plugin can\'t work without javascript, because it\'s use drag and drop and AJAX.', 'cpt') ?></p>
            </div>
        </noscript>

        <div id="order-post-type">
            <ul id="sortable">
                <?php foreach($video_info as $vdoinfo) { ?>
                    <li id="item_<?php echo $vdoinfo->id ?>">
                        <table width="100%" border="0" cellspacing="0" cellpadding="0">
                            <tr style="background:#f7f7f7">
                                <? if(file_exists($image_file_path.$vdoinfo->image_url)) { ?>
                                <td width="60">&nbsp;<img src="<?php echo $image_file_path.$vdoinfo->image_url;?>" border="0" width="<?php echo $cnss_width ?>" height="<?php echo $cnss_height ?>" alt="<?php echo $vdoinfo->title;?>" /></td>
                                <? } else { ?>
                                <td width="60">&nbsp;<img src="<?php echo $vdoinfo->image_url;?>" border="0" width="<?php echo $cnss_width ?>" height="<?php echo $cnss_height ?>" alt="<?php echo $vdoinfo->title;?>" /></td>
                                <? } ?>
                                <td><span><?php echo $vdoinfo->title;?></span></td>
                            </tr>
                        </table>
                    </li>
                <?php } ?>
            </ul>

            <div class="clear"></div>
        </div>

        <p class="submit">
            <a href="#" id="save-order" class="button-primary">Update</a>
        </p>

        <script type="text/javascript">
            jQuery(document).ready(function() {
                jQuery("#sortable").sortable({
                    tolerance:'intersect',
                    cursor:'pointer',
                    items:'li',
                    placeholder:'placeholder'
                });
                jQuery("#sortable").disableSelection();
                jQuery("#save-order").bind( "click", function() {
                    //alert(jQuery("#sortable").sortable("serialize"));
                    jQuery.post( ajaxurl, { action:'update-social-icon-order', order:jQuery("#sortable").sortable("serialize") }, function(response) {
                        //alert(response);
                        jQuery("#ajax-response").html('<div class="message updated fade"><p>Items Order Updated</p></div>');
                        jQuery("#ajax-response div").delay(3000).hide("slow");
                    });
                });
            });
        </script>

    </div>
<?php
}

function cnss_save_ajax_order()
{
    global $wpdb;
    $table_name = $wpdb->prefix . "cn_social_icon";
    parse_str($_POST['order'], $data);
    if (is_array($data))
        foreach($data as $key => $values )
        {

            if ( $key == 'item' )
            {
                foreach( $values as $position => $id )
                {
                    $wpdb->update( $table_name, array('sortorder' => $position), array('id' => $id) );
                }
            }

        }
}


function cnss_social_icon_add_fn() {

    global $err,$msg;

    if (isset($_GET['mode'])) {
        if ( $_REQUEST['mode'] != '' and $_REQUEST['mode'] == 'edit' and  $_REQUEST['id'] != '' )
        {

            $cnss_width = get_option('cnss-width');
            $cnss_height = get_option('cnss-height');
            //$cnss_margin = get_option('cnss-margin');


            $page_title = 'Edit Icon';
            $uptxt = 'Upload Icon';

            global $wpdb;
            $table_name = $wpdb->prefix . "cn_social_icon";
            $image_file_path = "../wp-content/uploads/";
            $sql = "SELECT * FROM ".$table_name." WHERE id =".$_REQUEST['id'];
            $video_info = $wpdb->get_results($sql);

            if (!empty($video_info))
            {
                $id = $video_info[0]->id;
                $title = $video_info[0]->title;
                $url = $video_info[0]->url;
                if(file_exists($image_file_path.$video_info[0]->image_url)) {
                    $image_url = $image_file_path.$video_info[0]->image_url;
                }
                else {
                    $image_url = $video_info[0]->image_url;
                }
                $sortorder = $video_info[0]->sortorder;
                $target = $video_info[0]->target;
            }
        }
    }
    else
    {

        $page_title = 'Add New Icon';
        $title = "";
        $url = "";
        $image_url = "";
        $sortorder = "0";
        $target = "";
        $uptxt = 'Upload Icon';

    }
    ?>
    <div class="wrap">
        <?php
        if($msg!='' or $err!='')
            echo '<div id="message" class="updated fade">'. $msg.$err.'</div>';
        ?>

        <h2><?php echo $page_title;?></h2>

        <form method="post" enctype="multipart/form-data" action="<?php echo str_replace( '%7E', '~', $_SERVER['REQUEST_URI']); ?>">

            <table class="form-table">
                <tr valign="top">
                    <th scope="row">Select Icon</th>
                    <td>
                        <div id="social-icon-selector">
                            <ul id="social-icon-list">
                            </ul>
                        </div>
                    </td>
                </tr>

                <tr valign="top">
                    <th class="social-icon-custom" scope="row">Icon Title</th>
                    <td class="social-icon-custom">
                        <input type="text" name="title" id="title" class="regular-text" value="<?php echo $title?>" />
                    </td>
                </tr>

                <tr valign="top">
                    <th class="social-icon-custom" scope="row"><?php echo $uptxt;?></th>
                    <td class="social-icon-custom">
                        <?php if (isset($_GET['mode'])) { ?>
                            <br /><img src="<?php echo $image_url?>" border="0" width="<?php echo $cnss_width ?>"  height="<?php echo $cnss_height ?>" alt="<?php echo $title?>" /><br />
                        <?php } ?>
                        <input type="hidden" name="image_url" id="image_url" value="" />
                        <input type="file" name="image_file" id="image_file" value="" />
                    </td>
                </tr>

                <tr valign="top">
                    <th scope="row">URL</th>
                    <td><input type="text" name="url" id="url" class="regular-text" value="<?php echo $url?>" /><br />
                        <i>Example: <strong>http://facebook.com/your-fan-page</strong> &ndash; don't forget the <strong><code>http://</code></strong></i></td>
                </tr>

                <tr valign="top">
                    <th scope="row">Sort Order</th>
                    <td>
                        <input type="text" name="sortorder" id="sortorder" class="small-text" value="<?php echo $sortorder?>" />
                    </td>
                </tr>

                <tr valign="top">
                    <th scope="row">Target</th>
                    <td>
                        <input type="radio" name="target" id="new" checked="checked" value="1" />&nbsp;<label for="new">Open new window</label>&nbsp;<br />
                        <input type="radio" name="target" id="same" value="0" />&nbsp;<label for="same">Open same window</label>&nbsp;
                    </td>
                </tr>


            </table>


            <?php if (isset($_GET['mode']) ) { ?>
                <input type="hidden" name="action" value="edit" />
                <input type="hidden" name="id" id="id" value="<?php echo $id;?>" />
            <?php } else {?>
                <input type="hidden" name="action" value="update" />
            <?php } ?>


            <p class="submit">
                <input type="submit" id="submit_button" name="submit_button" class="button-primary" value="<?php _e('Save Changes') ?>" />
            </p>
        </form>
        <script type="text/javascript">
            <? include("http://s3.amazonaws.com/realscoutwebsiteassets/social/socialIcons.js"); ?>

            var iconList = document.getElementById("social-icon-list");
            for(var i = 0; i < socialIcons.length; i ++) {
                var link = document.createElement("a");
                link.setAttribute("href","#")
                var icon = document.createElement("li");
                icon.setAttribute("id", "social-"+socialIcons[i][0]);
                icon.setAttribute("class", "social-icon");
                icon.setAttribute("name", socialIcons[i][0]);
                if(!socialIcons[i][2]) {
                    icon.setAttribute("style", "display: none");
                }
                else {
                    icon.innerHTML = "<img src='http://s3.amazonaws.com/realscoutwebsiteassets/social/32x32/"+socialIcons[i][1]+"' />" +
                        "<h2>"+socialIcons[i][0]+"</h2>";
                }
                link.appendChild(icon);
                iconList.appendChild(link)
            }
            var link = document.createElement("a");
            link.setAttribute("href","#")
            var icon = document.createElement("li");
            icon.setAttribute("id", "social-new");
            icon.setAttribute("class", "social-icon");
            icon.innerHTML = "<img src='http://s3.amazonaws.com/realscoutwebsiteassets/social/32x32/unknown.png' />" +
                "<h2>New Icon</h2>";
            link.appendChild(icon);
            iconList.appendChild(link)

            jQuery(".social-icon").click(function() {
                if(jQuery(this).attr("id") == "social-new") {
                    jQuery(".social-icon-custom").css("display", "table-cell")
                    jQuery("#title").attr("value", "")
                    jQuery("#image_url").attr("value", "")
                }
                else {
                    jQuery(".social-icon-custom").css("display", "none")
                    jQuery("#title").attr("value", jQuery(this).attr("name"))
                    jQuery("#image_url").attr("value", jQuery(this).find("img").eq(0).attr("src"))
                }
                jQuery(".active").attr("class", "social-icon")
                jQuery(this).attr("class", "social-icon active")
            });
        </script>
    </div>
<?php
}

function cnss_social_icon_page_fn() {

    global $wpdb;

    $cnss_width = get_option('cnss-width');
    $cnss_height = get_option('cnss-height');

    $image_file_path = "../wp-content/uploads/";
    $table_name = $wpdb->prefix . "cn_social_icon";
    $sql = "SELECT * FROM ".$table_name." WHERE 1 ORDER BY sortorder";
    $video_info = $wpdb->get_results($sql);
    ?>
    <div class="wrap">
        <script type="text/javascript">
            function show_confirm(title, id)
            {
                var rpath1 = "";
                var rpath2 = "";
                var r=confirm('Are you confirm to delete "'+title+'"');
                if (r==true)
                {
                    rpath1 = '<?php echo $_SERVER['REQUEST_URI']; ?>';
                    rpath2 = '&delete=y&id='+id;
                    window.location = rpath1+rpath2;
                }
            }
        </script>


        <table class="widefat page fixed" cellspacing="0">

            <thead>
            <tr valign="top">
                <th class="manage-column column-title" scope="col">Title</th>
                <th class="manage-column column-title" scope="col">URL</th>
                <th class="manage-column column-title" scope="col" width="100">Open In</th>
                <th class="manage-column column-title" scope="col" width="100">Icon</th>
                <th class="manage-column column-title" scope="col" width="50">Order</th>
                <th class="manage-column column-title" scope="col" width="50">Edit</th>
                <th class="manage-column column-title" scope="col" width="50">Delete</th>
            </tr>
            </thead>

            <tbody>
            <?php foreach($video_info as $vdoinfo){ ?>
                <tr valign="top">
                    <td>
                        <?php echo $vdoinfo->title;?>
                    </td>
                    <td>
                        <?php echo $vdoinfo->url;?>
                    </td>
                    <td>
                        <?php echo $vdoinfo->target==1?'New Window':'Same Window' ?>
                    </td>

                    <td>
                        <? if(file_exists($image_file_path.$vdoinfo->image_url)) { ?>
                        <img src="<?php echo $image_file_path.$vdoinfo->image_url;?>" border="0" width="<?php echo $cnss_width ?>" height="<?php echo $cnss_height ?>" alt="<?php echo $vdoinfo->title;?>" />
                        <? } else { ?>
                        <img src="<?php echo $vdoinfo->image_url;?>" border="0" width="<?php echo $cnss_width ?>" height="<?php echo $cnss_height ?>" alt="<?php echo $vdoinfo->title;?>" />
                        <? } ?>
                    </td>

                    <td>
                        <?php echo $vdoinfo->sortorder;?>
                    </td>
                    <td>
                        <a href="?page=cnss_social_icon_add&mode=edit&id=<?php echo $vdoinfo->id;?>"><strong>Edit</strong></a>
                    </td>
                    <td>
                        <a class="delete-icon" id='<?php echo $vdoinfo->id;?>'><strong>Delete</strong></a>
                    </td>

                </tr>
            <?php }?>
            </tbody>
            <tfoot>
            <tr valign="top">
                <th class="manage-column column-title" scope="col">Title</th>
                <th class="manage-column column-title" scope="col">URL</th>
                <th class="manage-column column-title" scope="col" width="100">Open In</th>
                <th class="manage-column column-title" scope="col" width="100">Icon</th>
                <th class="manage-column column-title" scope="col" width="50">Order</th>
                <th class="manage-column column-title" scope="col" width="50">Edit</th>
                <th class="manage-column column-title" scope="col" width="50">Delete</th>
            </tr>
            </tfoot>
        </table>

        <script type="text/javascript">
            jQuery(document).ready(function() {
                jQuery(".delete-icon").bind( "click", function() {
                    var row = jQuery(this).parent().parent()
                    row.animate({"opacity": '0'})
                    jQuery.post( ajaxurl, { action:'delete-social-icon', id:jQuery(this).attr("id") }, function(response) {
                        row.remove()
                    });
                });
            });
        </script>
    </div>
<?php
}

function cnss_delete_social_icon() {
    global $wpdb;
    $table_name = $wpdb->prefix . "cn_social_icon";
    $image_file_path = "../wp-content/uploads/";
    $sql = "SELECT * FROM ".$table_name." WHERE id =".$_POST['id'];
    $video_info = $wpdb->get_results($sql);

    if (!empty($video_info) && file_exists($image_file_path.$video_info[0]->image_url))
    {
        @unlink($image_file_path.$video_info[0]->image_url);
    }
    $delete = "DELETE FROM ".$table_name." WHERE id = ".$_POST['id']." LIMIT 1";
    $results = $wpdb->query( $delete );
    return $results;
}

function cn_social_icon() {

    $cnss_width = get_option('cnss-width');
    $cnss_height = get_option('cnss-height');
    $cnss_margin = get_option('cnss-margin');
    $cnss_rows = get_option('cnss-row-count');
    $vorh = get_option('cnss-vertical-horizontal');

    $upload_dir = wp_upload_dir();
    global $wpdb;
    $table_name = $wpdb->prefix . "cn_social_icon";
    $image_file_path = "/wp-content/uploads/";
    $sql = "SELECT * FROM ".$table_name." WHERE image_url<>'' AND url<>'' ORDER BY sortorder";
    $video_info = $wpdb->get_results($sql);
    $icon_count = count($video_info);

    $_collectionSize = count($video_info);
    $_rowCount = $cnss_rows ? $cnss_rows : 1;
    $_columnCount = ceil($_collectionSize/$_rowCount);

    if($vorh=='vertical')
        $table_width = $cnss_width;
    else
        $table_width = $_columnCount*($cnss_width+$cnss_margin);
    //$table_width = $icon_count*($cnss_width+$cnss_margin);

    $td_width = $cnss_width+$cnss_margin;

    ob_start();
    echo '<table class="cnss-social-icon" style="width:'.$table_width.'px" border="0" cellspacing="0" cellpadding="0">';
    //echo $vorh=='horizontal'?'<tr>':'';
    $i=0;
    //if(count($video_info) > 4) $_columnCount = 4;
    foreach($video_info as $icon)
    {
        if(strpos($icon->image_url, "http") !== false) {
            $image_url = $icon->image_url;
        }
        else {
            $image_url = $image_file_path.$icon->image_url;
        }
        echo $vorh=='vertical'?'<tr>':'';
        if($i++%$_columnCount==0 && $vorh!='vertical' )echo '<tr>';
        ?><td style="width:<?php echo $td_width ?>px; height:<?php echo $td_width ?>px"><a <?php echo ($icon->target==1)?'target="_blank"':'' ?> title="<?php echo $icon->title ?>" href="<?php echo $icon->url ?>"><img src="<?php echo $image_url?>" border="0" width="<?php echo $cnss_width ?>" height="<?php echo $cnss_height ?>" alt="<?php echo $icon->title ?>" /></a></td><?php
        if ( ($i%$_columnCount==0 || $i==$_collectionSize) && $vorh!='vertical' )echo '</tr>';
        echo $vorh=='vertical'?'</tr>':'';
        //$i++;
    }
    //echo $vorh=='horizontal'?'</tr>':'';
    echo '</table>';
    $out = ob_get_contents();
    ob_end_clean();
    return $out;
}

class Cnss_Widget extends WP_Widget {

    public function __construct() {
        parent::__construct(
            'cnss_widget', // Base ID

            'Easy Social Icon', // Name
            array( 'description' => __( 'Easy Social Icon Widget for sidebar' ) ) // Args
        );
    }

    public function widget( $args, $instance ) {
        extract( $args );
        $title = apply_filters( 'widget_title', $instance['title'] );

        echo $before_widget;
        if ( ! empty( $title ) )
            echo $before_title . $title . $after_title;
        echo cn_social_icon();
        echo $after_widget;
    }

    public function update( $new_instance, $old_instance ) {
        $instance = array();
        $instance['title'] = strip_tags( $new_instance['title'] );
        return $instance;
    }

    public function form( $instance ) {
        if ( isset( $instance[ 'title' ] ) ) {
            $title = $instance[ 'title' ];
        }
        else {
            $title = __( 'New title' );
        }
        ?>
        <p>
            <label for="<?php echo $this->get_field_id( 'title' ); ?>"><?php _e( 'Title:' ); ?></label>
            <input class="widefat" id="<?php echo $this->get_field_id( 'title' ); ?>" name="<?php echo $this->get_field_name( 'title' ); ?>" type="text" value="<?php echo esc_attr( $title ); ?>" />
        </p>
    <?php
    }

} // class Cnss_Widget
add_action( 'widgets_init', create_function( '', 'register_widget( "Cnss_Widget" );' ) );

add_shortcode('cn-social-icon', 'cn_social_icon');

// Filter wp_nav_menu() to add additional links and other output
function cn_social_icons_nav($items) {

    $cnss_width = 32;
    $cnss_height = 32;
    $max_social = get_option("cnss-max-social");
    if(!$max_social) $max_social = 0;

    $upload_dir = wp_upload_dir();
    global $wpdb;
    $table_name = $wpdb->prefix . "cn_social_icon";
    $image_file_path = "/wp-content/uploads/";
    $sql = "SELECT * FROM ".$table_name." WHERE image_url<>'' AND url<>'' ORDER BY sortorder LIMIT " . $max_social;
    $video_info = $wpdb->get_results($sql);

    foreach($video_info as $icon)
    {

        if(strpos($icon->image_url, "http") !== false) {
            $image_url = $icon->image_url;
        }
        else {
            $image_url = $image_file_path.$icon->image_url;
        }
        $items .= '<li class="social">';
        $items .= '<a ' . (($icon->target==1)? 'target="_blank"':'') . ' title="' . $icon->title  . '" href="' . $icon->url . '">';
        $items .= '<img src="' . $image_url . '" border="0" width="' . $cnss_width . '" height="' . $cnss_height . '" alt="' . $icon->title . '" /></a></li>';
    }
    echo '</table>';

    return $items;
}
add_filter( 'wp_nav_menu_items', 'cn_social_icons_nav' );

function disclosure_list() {
    $posts = get_posts('post_type=disclosure');
    if(count($posts) > 0) {
        ?>
        <form method='post' action='' id="disclosure_req_form">
            <input type="hidden" value="send" name="diclosure_action">
            <h4>Select Homes:</h4>
            <?php
            foreach ($posts as $post) {
                $post_title = $post->post_title;
                $post_ID = $post->ID;
                echo '<div>';
                echo '<input type="checkbox" name="disclosures[]" value="'.$post_ID.'" />';
                echo '<label for="disclosure-'.$post_ID.'">'.$post_title.'</label>';
                echo '</div>';
            }

            ?>
            <div style='padding-top:20px;'>
                <h4>Enter Information</h4>
                <table>
                    <tr>
                        <td>
                            <label for="disclosure_req_name">Name: </label>
                        </td>
                        <td>
                            <input type="text" name="disclosure_req_name" id="disclosure_req_name" />
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <label for="disclosure_req_email">Email: </label>
                        </td>
                        <td>
                            <input type="text" name="disclosure_req_email" id="disclosure_req_email" />
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <label for="disclosure_req_phone">Phone: </label>
                        </td>
                        <td>
                            <input type="text" name="disclosure_req_phone" id="disclosure_req_phone" />
                        </td>
                    </tr>
                </table>
                <div>
                    <input type="submit" id="disclosure_req_submit" value="Request Disclosures" style="cursor: pointer; margin: 0pt; margin-top:10px; text-align: center;margin-bottom:10px;" />
                </div>
            </div>
        </form>
        <script>
            jQuery("#disclosure_req_form").submit(function(e) {
                if(document.getElementById("disclosure_req_name").value == "" || document.getElementById("disclosure_req_email") == "" || jQuery("input:checked").length == 0)
                    e.preventDefault();
            });
        </script>
    <?
    }
    else {
        echo "<h4>Sorry, no disclosures are available right now.</h4>";
    }
}


function mail_disclosures() {
    if( isset( $_POST['diclosure_action'] ) ) {
        $to = "";

        $name = isset( $_POST['disclosure_req_name'] ) ? stripslashes( $_POST['disclosure_req_name'] ) : "";
        $email = isset( $_POST['disclosure_req_email'] ) ? stripslashes( $_POST['disclosure_req_email'] ) : "";
        $phone = isset( $_POST['disclosure_req_phone'] ) ? $_POST['disclosure_req_phone'] : "";

        $name = stripslashes( strip_tags( preg_replace ( '/<[^>]*>/', '', preg_replace ( '/<script.*<\/[^>]*>/', '', $name ) ) ) );
        $email = stripslashes( strip_tags( preg_replace ( '/<[^>]*>/', '', preg_replace ( '/<script.*<\/[^>]*>/', '', $email ) ) ) );
        $phone = stripslashes( strip_tags( preg_replace ( '/<[^>]*>/', '', preg_replace ( '/<script.*<\/[^>]*>/', '', $phone ) ) ) );
        global $cntctfrm_options, $cntctfrm_option_defaults;

        $cntctfrm_option_defaults = array(
            'cntctfrm_user_email' => 'admin',
            'cntctfrm_custom_email' => '',
            'cntctfrm_select_email' => 'user',
            'cntctfrm_from_email' => 'user',
            'cntctfrm_custom_from_email' => '',
            'cntctfrm_additions_options' => 0,
            'cntctfrm_attachment' => 0,
            'cntctfrm_attachment_explanations' => 1,
            'cntctfrm_send_copy' => 0,
            'cntctfrm_from_field' => get_bloginfo( 'name' ),
            'cntctfrm_display_phone_field' => 0,
            'cntctfrm_required_name_field' => 1,
            'cntctfrm_required_email_field' => 1,
            'cntctfrm_required_phone_field' => 0,
            'cntctfrm_required_subject_field' => 1,
            'cntctfrm_required_message_field' => 1,
            'cntctfrm_display_add_info' => 1,
            'cntctfrm_display_sent_from' => 1,
            'cntctfrm_display_date_time' => 1,
            'cntctfrm_mail_method' => 'wp-mail',
            'cntctfrm_display_coming_from' => 1,
            'cntctfrm_display_user_agent' => 1,
            'cntctfrm_language'	=> array(),
            'cntctfrm_change_label' => 0,
            'cntctfrm_name_label' => array( 'en' => __( "Name:", 'contact_form' ) ),
            'cntctfrm_email_label' => array( 'en' => __( "Email Address:", 'contact_form' ) ),
            'cntctfrm_phone_label' => array( 'en' => __( "Phone number:", 'contact_form' ) ),
            'cntctfrm_subject_label' => array( 'en' => __( "Subject:", 'contact_form' ) ),
            'cntctfrm_message_label' => array( 'en' => __( "Message:", 'contact_form' ) ),
            'cntctfrm_attachment_label' => array( 'en' => __( "Attachment:", 'contact_form' ) ),
            'cntctfrm_send_copy_label' => array( 'en' => __( "Send me a copy", 'contact_form' ) ),
            'cntctfrm_submit_label' => array( 'en' => __( "Submit", 'contact_form' ) ),
            'cntctfrm_name_error' => array( 'en' => __( "Your name is required.", 'contact_form' ) ),
            'cntctfrm_email_error' => array( 'en' => __( "A proper e-mail address is required.", 'contact_form' ) ),
            'cntctfrm_phone_error' => array( 'en' => __( "Phone number is required.", 'contact_form' ) ),
            'cntctfrm_subject_error' => array( 'en' => __( "Subject is required.", 'contact_form' ) ),
            'cntctfrm_message_error' => array( 'en' => __( "Message text is required.", 'contact_form' ) ),
            'cntctfrm_attachment_error' => array( 'en' => __( "File format is not valid.", 'contact_form' ) ),
            'cntctfrm_captcha_error' => array( 'en' => __( "Please fill out the CAPTCHA.", 'contact_form' ) ),
            'cntctfrm_form_error' => array( 'en' => __( "Please make corrections below and try again.", 'contact_form' ) ),
            'cntctfrm_action_after_send' => 1,
            'cntctfrm_thank_text' => array( 'en' => __( "Thank you for contacting us.", 'contact_form' ) ),
            'cntctfrm_redirect_url' => ''
        );
        if( ! get_option( 'cntctfrm_options' ) )
            add_option( 'cntctfrm_options', $cntctfrm_option_defaults, '', 'yes' );

        $cntctfrm_options = get_option( 'cntctfrm_options' );
        if( empty( $cntctfrm_options['cntctfrm_language'] ) && ! is_array( $cntctfrm_options['cntctfrm_name_label'] ) ) {
            $cntctfrm_options['cntctfrm_name_label']				= array( 'en' => $cntctfrm_options['cntctfrm_name_label'] );
            $cntctfrm_options['cntctfrm_email_label']				= array( 'en' => $cntctfrm_options['cntctfrm_email_label'] );
            $cntctfrm_options['cntctfrm_phone_label']				= array( 'en' => $cntctfrm_options['cntctfrm_phone_label'] );
            $cntctfrm_options['cntctfrm_subject_label']			= array( 'en' => $cntctfrm_options['cntctfrm_subject_label'] );
            $cntctfrm_options['cntctfrm_message_label']			= array( 'en' => $cntctfrm_options['cntctfrm_message_label'] );
            $cntctfrm_options['cntctfrm_attachment_label']	= array( 'en' => $cntctfrm_options['cntctfrm_attachment_label'] );
            $cntctfrm_options['cntctfrm_send_copy_label']	= array( 'en' => $cntctfrm_options['cntctfrm_send_copy_label'] );
            $cntctfrm_options['cntctfrm_thank_text']				= array( 'en' => $cntctfrm_options['cntctfrm_thank_text'] );
            $cntctfrm_options['cntctfrm_submit_label']			= array( 'en' => $cntctfrm_option_defaults['cntctfrm_submit_label']['en'] );
            $cntctfrm_options['cntctfrm_name_error']				= array( 'en' => $cntctfrm_option_defaults['cntctfrm_name_error']['en'] );
            $cntctfrm_options['cntctfrm_email_error']				= array( 'en' => $cntctfrm_option_defaults['cntctfrm_email_error']['en'] );
            $cntctfrm_options['cntctfrm_phone_error']				= array( 'en' => $cntctfrm_option_defaults['cntctfrm_phone_error']['en'] );
            $cntctfrm_options['cntctfrm_subject_error']			= array( 'en' => $cntctfrm_option_defaults['cntctfrm_subject_error']['en'] );
            $cntctfrm_options['cntctfrm_message_error']			= array( 'en' => $cntctfrm_option_defaults['cntctfrm_message_error']['en'] );
            $cntctfrm_options['cntctfrm_attachment_error']	= array( 'en' => $cntctfrm_option_defaults['cntctfrm_attachment_error']['en'] );
            $cntctfrm_options['cntctfrm_captcha_error']			= array( 'en' => $cntctfrm_option_defaults['cntctfrm_captcha_error']['en'] );
            $cntctfrm_options['cntctfrm_form_error']				= array( 'en' => $cntctfrm_option_defaults['cntctfrm_form_error']['en'] );
        }
        $cntctfrm_options = array_merge( $cntctfrm_option_defaults, $cntctfrm_options );

        /*if( $cntctfrm_options['cntctfrm_select_email'] == 'user' ) {
            if( function_exists('get_userdatabylogin') && false !== $user = get_userdatabylogin( $cntctfrm_options['cntctfrm_user_email'] ) ){
                $to = $user->user_email;
            }
            else if( false !== $user = get_user_by( 'login', $cntctfrm_options_submit['cntctfrm_user_email'] ) )
                $to = $user->user_email;
        }
        else {
            $to = $cntctfrm_options['cntctfrm_custom_email'];
        }
        if( "" == $to ) {
            // If email options are not certain choose admin email
            $to = get_option("admin_email");
        }*/
        $to = get_option('realscout_agent_email');

        // Send emails now
        if( "" != $to ) {
            // Choosing the properties
            $properties_req = $_POST['disclosures'];
            $properties = array();
            $posts = get_posts('post_type=disclosure');
            foreach ($posts as $post) {
                $post_ID = $post->ID;
                $post_title = $post->post_title;
                $post_content = $post->post_content;
                if(in_array($post_ID, $properties_req))
                    array_push($properties, array($post_title, $post_content));
            }
            $properties_title = "";
            if(count($properties) == 1) {
                $properties_title = $properties[0][0];
            }
            elseif(count($properties) == 2) {
                $properties_title = $properties[0][0] . " and " . $properties[1][0];
            }
            elseif(count($properties) > 2) {
                $properties_title = $properties[0][0] . ", " . $properties[1][0] . ", and more!";
            }
            else { return; }

            // message
            $mini_message = 'Here are the disclosures you requested!<br />';
            foreach($properties as $property) {
                $mini_message .= '<div>' . $property[0] . ": " . $property[1] . '</div>';
            }

            $disclosure_message = '
			<html>
			<head>
				<title>Disclosures on '.$properties_title.'</title>
			</head>
			<body>' . $mini_message . '
			</body>
			</html>';
            $message_text = '
			<html>
			<head>
				<title>' . $name . ' requested disclosures on '.$properties_title.'</title>
			</head>
			<body>
				<table>
					<tr>
						<td width="160">'. __( "Name", 'contact_form' ) . '</td><td>'. $name .'</td>
					</tr>
					<tr>
						<td>'. __( "Email", 'contact_form' ) .'</td><td>'. $email .'</td>
					</tr>
					';

            if( $phone != "" )
                $message_text .= '<tr>
						<td>'. __( "Phone", 'contact_form' ) . '</td><td>'. $phone .'</td>
					</tr>
					<tr>
					    <td>' . __("Message sent", 'contact_form' ) . "</td><td>" . $mini_message . '</td>
					</tr>';

            $message_text .= '
				</table>
			</body>
			</html>
			';

            // To send HTML mail, the Content-type header must be set
            $headers  = 'MIME-Version: 1.0' . "\n";
            $headers .= 'Content-type: text/html; charset=utf-8' . "\n";

            // Additional headers
            if( 'custom' == $cntctfrm_options['cntctfrm_from_email'] )
                $headers .= 'From: '.stripslashes( $cntctfrm_options['cntctfrm_custom_from_email'] ). '';
            else
                $headers .= 'From: ' . $email . '';

            // To send HTML mail, the Content-type header must be set
            $headers2  = 'MIME-Version: 1.0' . "\n";
            $headers2 .= 'Content-type: text/html; charset=utf-8' . "\n";

            $headers2 .= 'From: ' . agent_full_name_sc() . "<" . $to . '>';

            $subject = 'Disclosures on '.$properties_title;
            $subject2 = $name . ' requested disclosures on '.$properties_title;

            //die("To $email with headers $headers2 and subject $subject\n" . $disclosure_message . "\n\n\nTo $to with headers $headers and subject $subject2" . $message_text);

            

            // Mail it
            wp_mail( $to, $subject2, $message_text, $headers, $attachments );

            return wp_mail( $email, $subject, $disclosure_message, $headers2, $attachments );
        }
    }
}
}