#!/usr/bin/env python

import roslib
import rospy

from subprocess import call
from std_msgs.msg import String

import roslib; roslib.load_manifest('sound_play')
from sound_play.msg import SoundRequest
from sound_play.libsoundplay import SoundClient



def listener():
	rospy.init_node('audio_messenger', anonymous = True)
	rospy.Subscriber("audio_say", String, callback)
	rospy.Subscriber("audio_wav", String, wavHandler)
	rospy.spin()

def callback(data):
	rospy.loginfo(rospy.get_name() + ": I heard %s" % data.data)
	soundhandle = SoundClient()
	rospy.sleep(1)

	if data.data == "'msdc'":
		while not rospy.is_shutdown():
			soundhandle.stopAll()
			try:
				rospy.sleep(.1)
			except:
				pass
		rospy.sleep(1)
	
	else:
		voice = 'voice_kal_diphone'
	
		s = data.data
		soundhandle.say(s, voice)
		rospy.sleep(1)

def wavHandler(data):
	soundhandle = SoundClient()
	rospy.sleep(1)
	soundhandle.playWave(data.data)
	rospy.sleep(1)

if __name__ == '__main__':
	listener()
