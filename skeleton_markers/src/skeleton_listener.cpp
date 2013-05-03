#include <ros/ros.h>
#include <tf/transform_listener.h>
#include <skeleton_markers/Skeleton.h>
#include "geometry_msgs/Twist.h"
#include <XnOpenNI.h>
#include <math.h>
#include <std_msgs/String.h>

int main(int argc, char** argv){
  ros::init(argc, argv, "my_tf_listener");
  ros::NodeHandle node;
	
  ros::Publisher whattosay = 
       node.advertise<std_msgs::String>("/audio_say", 1);

	tf::TransformListener listener_head;
	tf::TransformListener listener_RH;
	tf::TransformListener listener_LH;	
	tf::TransformListener listener_righthip;
	tf::TransformListener listener_lefthip;
	tf::TransformListener listener_torso;
	tf::TransformListener listener_neck;
	tf::TransformListener listener_Lknee;
	static tf::StampedTransform transform_head;
	static tf::StampedTransform transform_RH;
	static tf::StampedTransform transform_LH;
	static tf::StampedTransform transform_righthip;
	static tf::StampedTransform transform_lefthip;
	static tf::StampedTransform transform_torso;
	static tf::StampedTransform transform_neck;
	static tf::StampedTransform transform_Lknee;
	
	bool arr[4] = {false};
	for(int i=0; i<4; i++){
		arr[i] = false;
	}
    
	std_msgs::String msg;
	int count = 0;
	int current = 0;
    ros::Rate rate(5.0);
    
    while (node.ok()){
	count = 0;
        
        try{
            listener_RH.lookupTransform("/openni_depth_frame", "/head_1",
                               ros::Time(0), transform_head);
        }
        catch (tf::TransformException ex){
     				 ROS_ERROR("%s",ex.what());
        }
        
        try{
            listener_RH.lookupTransform("/openni_depth_frame", "/right_hand_1",
                               ros::Time(0), transform_RH);
        }
        catch (tf::TransformException ex){
     				 ROS_ERROR("%s",ex.what());
        }
        
        try{
            listener_LH.lookupTransform("/openni_depth_frame", "/left_hand_1",
                               ros::Time(0), transform_LH);
        }
        catch (tf::TransformException ex){
     				 ROS_ERROR("%s",ex.what());
        }
        
        try{
            listener_LH.lookupTransform("/openni_depth_frame", "/torso_1",
                               ros::Time(0), transform_torso);
        }
        catch (tf::TransformException ex){
     				 ROS_ERROR("%s",ex.what());
        }
			
        try{
            listener_LH.lookupTransform("/openni_depth_frame", "/neck_1",
                                ros::Time(0), transform_neck);
        }
        catch (tf::TransformException ex){
     				 ROS_ERROR("%s",ex.what());
        }	

        try{
            listener_Lknee.lookupTransform("/openni_depth_frame", "/left_knee_1",
                               ros::Time(0), transform_Lknee);
        }
        catch (tf::TransformException ex){
     				 ROS_ERROR("%s",ex.what());
        }
        
        try{
            listener_lefthip.lookupTransform("/openni_depth_frame", "/left_hip_1",
                               ros::Time(0), transform_lefthip);
        }
        catch (tf::TransformException ex){
     				 ROS_ERROR("%s",ex.what());
        }	
	
	
	std::stringstream ss;
    if((transform_LH.getOrigin().z() > transform_head.getOrigin().z()) == 1){
        std::cout<<"right hand high five!"<<std::endl;
        if(arr[0] == false){
            ss << "right hand high five";
    		msg.data = ss.str();
            current = count;
            whattosay.publish(msg);
            ROS_INFO("%s", msg.data.c_str());
                for(int i=0; i<4; i++){
                    arr[i] = false;
                }
            arr[0] = true;
        }
    }
	

    if((transform_RH.getOrigin().z()>transform_head.getOrigin().z()) == 1){
        std::cout<<"left hand high five!"<<std::endl;
        if(arr[1] == false){
            ss << "left hand high five";
            msg.data = ss.str();
            current = count;
            whattosay.publish(msg);
            ROS_INFO("%s", msg.data.c_str());
            for(int i=0; i<4; i++){
                arr[i] = false;
            }
            arr[1] = true;
        }
	}

    //uncomment below for debugging purposes
	static double rightHandZ = transform_LH.getOrigin().z();
	static double leftHandZ = transform_RH.getOrigin().z();
	static double torsoZ = transform_torso.getOrigin().z();
	static double neckZ = transform_neck.getOrigin().z();
	if((rightHandZ-(torsoZ+.05)) < .2  == 1){
		//std::cout<<"Right Hand height correct"<<std::endl;
        if(transform_RH.getOrigin().z() < transform_torso.getOrigin().z() == 1){
			//std::cout<<"Left Hand lower than torso"<<std::endl;
			if(fabs(transform_torso.getOrigin().y()-transform_LH.getOrigin().y()) < .1 == 1){
				std::cout<<"national anthem!"<<std::endl;
				if(arr[2] == false){
					ss << "god bless america";
                    msg.data = ss.str();
					current = count;
					whattosay.publish(msg);
					ROS_INFO("%s", msg.data.c_str());
					for(int i=0; i<4; i++){
						arr[i] = false;
					}
					arr[2] = true;
				}
			}
		}
	}

    //uncomment below for debugging
	if(transform_lefthip.getOrigin().z()-transform_Lknee.getOrigin().z() < .1 == 1){
		//std::cout<<"kungfu knee"<<std::endl;
		if(fabs(transform_LH.getOrigin().z()-transform_RH.getOrigin().z()) <.1 == 1){
			//std::cout<<"hand heights yoga"<<std::endl;
			if(fabs(transform_LH.getOrigin().y()-transform_RH.getOrigin().y()) <.1 == 1){
				std::cout<<"kung foo"<<std::endl;
				if(arr[3] == false){
					ss << "kung foo";
                    msg.data = ss.str();
					current = count;
					whattosay.publish(msg);
					ROS_INFO("%s", msg.data.c_str());
					for(int i=0; i<4; i++){
						arr[i] = false;
					}
					arr[3] = true;
				}
			} 		
		}  
	}


    ros::spinOnce();
	count++;
    rate.sleep();
	
  }
  return 0;
};
