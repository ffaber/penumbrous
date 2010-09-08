# Copyright (C) 2010 Fred Faber.
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
# http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.

#################################################################
#  Basic Keystrings
#################################################################
alias a            alias
#alias h            'history \!* | tail  -50 | more'
alias h            'history'
alias cls 		'clear'	

alias cd            'cd \!*;echo $cwd'
alias pd pushd
alias pp popd
alias drs 'dirs -v'
alias pdw pwd

alias rm            'rm -i'

alias ls 'ls -aF'
alias dir ls
alias ll 'ls -l'
alias li 'ls -ltr --color'
alias lie 'ls -ltr'
alias lii li
alias lsc 'ls --color'
alias lsz 'ls -lShr'

alias more less
alias mroe more
alias whereami 'pwd'
alias onwhatami 'hostname'
#################################################################
# Commands
#################################################################
alias showfonts xlsfonts

#################################################################

#################################################################
#helpful aliases:
#################################################################

alias xlock 'xlock -mode random'
alias ff 'fortune 100% calvin'
# alias em 'emacs -g 165x60+12+06 \!* &'
alias em 'emacs \!* &'
alias emdg 'em --debug-init'
#alias em 'emacs -g 250x87+12+06 \!* &'
#123x51(ish)
#alias em 'emacs -geometry 132x48+12+06 \!* &'
#alias em 'emacs -geometry 132x49+0+0 \!* &'
#alias em 'emacs \!* &'

alias vi vim
alias iv vi

alias psg  'ps -ef | grep \!* | grep -v grep'
alias psgk 'psg \!:1 | awk "{print $2}" | xargs kill -\!:2'
alias desk 'pd /mnt/windows/windows/Desktop'

alias thttp \'tail -f /usr/local/apache/logs/access_log\'
alias sra 'source ~/.aliases'

alias xterm-colors 'view /usr/X11R6/lib/X11/rgb.txt'

alias ixt 'xterm  -geometry 83x56+12+4 -bg DarkSlateGray -fg Wheat -sb -sl 1000 -fn "-misc-fixed-medium-r-normal--13-100-100-100-c-70-iso8859-1" &'
alias ixtw 'xterm  -geometry 141x56-4+3 -bg DarkSlateGray -fg Wheat -sb -sl 1000 -fn "-misc-fixed-medium-r-normal--13-100-100-100-c-70-iso8859-1" &'
alias ixt2 'xterm  -geometry 86x55+471+17 -bg DarkSlateGray -fg Wheat -sb -sl 1000 -fn "-misc-fixed-medium-r-normal--13-100-100-100-c-70-iso8859-1" &'

alias bxt 'xterm  -geometry 83x56+12+4 -bg black -fg green -sb -sl 1000 -fn "-misc-fixed-medium-r-normal--13-100-100-100-c-70-iso8859-1" &'
alias bxtw 'xterm  -geometry 141x56-4+3 -bg black -fg green -sb -sl 1000 -fn "-misc-fixed-medium-r-normal--13-100-100-100-c-70-iso8859-1" &'
alias bxt2 'xterm  -geometry 86x55+471+17 -bg black -fg green -sb -sl 1000 -fn "-misc-fixed-medium-r-normal--13-100-100-100-c-70-iso8859-1" &'
alias lxt 'xterm  -geometry 83x56+12+4 -bg white -fg red -sb -sl 1000 -fn "-misc-fixed-medium-r-normal--13-100-100-100-c-70-iso8859-1" &'
alias lxtw 'xterm  -geometry 141x56-4+3 -bg white -fg red -sb -sl 1000 -fn "-misc-fixed-medium-r-normal--13-100-100-100-c-70-iso8859-1" &'
alias lxt2 'xterm  -geometry 86x55+471+17 -bg white -fg red -sb -sl 1000 -fn "-misc-fixed-medium-r-normal--13-100-100-100-c-70-iso8859-1" &'

#alias ixt 'xterm  -geometry 74x50+12+4 -bg DarkSlateGray -fg Wheat -sb -sl 1000 -fn "-misc-fixed-medium-r-normal--14-110-100-100-c-70-iso8859-1"'
#alias ixt2 'xterm  -geometry 74x50+471+17 -bg DarkSlateGray -fg Wheat -sb -sl 1000 -fn "-misc-fixed-medium-r-normal--14-110-100-100-c-70-iso8859-1"'
alias ikon 'konsole  -vt_sz 74x50'


#alias bxtgs 'xterm -geometry 120x90+12+4 -bg black -fg green -sb -sl 1000 -fn "-misc-fixed-medium-r-normal--13-100-100-100-c-70-iso8859-1" -exec tcsh &'
#alias bxtgl 'xterm -geometry 120x90+12+4 -bg black -fg green -sb -sl 1000 -fn "-misc-fixed-medium-r-normal--14-130-75-75-c-70-iso8859-9" -exec tcsh &'
#alias bxtgl2 'xterm -geometry 120x90+12+4 -bg black -fg green -sb -sl 1000 -fn "-misc-fixed-medium-r-normal--15-140-75-75-c-90-iso8859-9" -exec tcsh &'
alias bxtg 'xterm -geometry 97x82+12+4 -bg black -fg green -sb -sl 1000 -fn "-misc-fixed-medium-r-normal--15-140-75-75-c-90-iso8859-1" -exec tcsh &'
alias bxtg2 'xterm -geometry 97x82+900+4 -bg black -fg green -sb -sl 1000 -fn "-misc-fixed-medium-r-normal--15-140-75-75-c-90-iso8859-1" -exec tcsh &'

alias bxtnx 'xterm -geometry 89x67+6+3 -bg black -fg green -sb -sl 1000 -fn "-misc-fixed-medium-r-normal--12-100-100-100-c-70-iso8859-1" -exec tcsh &'
alias bxtnx2 'xterm -geometry 84x67+587+5 -bg black -fg green -sb -sl 1000 -fn "-misc-fixed-medium-r-normal--13-100-100-100-c-70-iso8859-1" -exec tcsh &'


alias ixtg 'xterm -geometry 97x82+12+4 -bg DarkSlateGray -fg Wheat -sb -sl 1000 -fn "-misc-fixed-medium-r-normal--15-140-75-75-c-90-iso8859-1" -exec tcsh &'
alias ixtg2 'xterm -geometry 97x82+900+4 -bg DarkSlateGray -fg Wheat -sb -sl 1000 -fn "-misc-fixed-medium-r-normal--15-140-75-75-c-90-iso8859-1" -exec tcsh &'

alias blxtg 'xterm -geometry 97x82+12+4 -sb -sl 1000 -fn "-misc-fixed-medium-r-normal--15-140-75-75-c-90-iso8859-1" -bg SteelBlue3 -fg maroon -exec tcsh &'

alias bxtg3 'xterm -geometry 97x82+120+4000 -bg SteelBlue3 -fg maroon -sb -sl 1000 -fn "-misc-fixed-medium-r-normal--15-140-75-75-c-90-iso8859-1" -exec tcsh & '
alias bxtgr 'xterm -geometry 97x82+120+4000 -bg Black -fg red1 -sb -sl 1000 -fn "-misc-fixed-medium-r-normal--15-140-75-75-c-90-iso8859-1" -exec tcsh &'
alias lxtgw 'xterm -geometry 208x50+12+200 -bg DarkSlateGray -fg Wheat -sb -sl 1000 -fn "-misc-fixed-medium-r-normal--15-140-75-75-c-90-iso8859-1" -exec tcsh &'

alias httpd_up '/etc/rc.d/init.d/httpd start &'
alias fgrp 'echo find . -type f -exec grep "\!^" {} \; -print; find . -type f -exec grep "\!^" {} \; -print'
alias ffle 'echo find . -type f -name "\!^" -print; find . -type f -name "\!^" -print'
alias hgrp 'h | grep \!*'

alias makehere 'if ($?PS1 == 0 && `ssh-add -l | grep $USER` == "") ssh-add; \makehere \!*'
alias cu 'cat ./_current';

alias blt 'bst `echo \!:1 | sed "s/javatests\///;s/java\///;s/\//\./g;s/.java//"`'
alias bltd 'bst --debug=\!:1 `echo \!:2 | sed "s/javatests\///;s/java\///;s/\//\./g;s/.java//"`'

# mysl commands
# show innodb status, show table status, show full processlist, 
alias laddbs 'laddbr -e "show innodb status"'
alias mse 'mysqladmin extended -u root -p '
alias msei 'mysqladmin extended -i10 -r -u root -p '
alias mspl 'mysqladmin processlist -u root -p '
alias msins 'mysql -u root -p -e "show innodb status" '

alias tdiff '/usr/pubsw/bin/tdiff -w'
alias p4td 'setenv P4DIFF_orig $P4DIFF; unsetenv P4DIFF; setenv P4DIFF "/usr/pubsw/bin/tdiff -w"'
alias unp4td 'unsetenv P4DIFF; setenv P4DIFF $P4DIFF_orig; unsetenv P4DIFF;'
 
alias x_src 'pd /var/www/html/xoops-2.0.2/html/'
alias pdx 'pushd /var/www/html/xoops-2.0.2/html/modules/'
alias grpe grep
alias grpe grep
alias pow apm
alias mz '/usr/local/mozilla/mozilla &'
alias vr 'vim -R \!*'
alias pdn 'pd /var/www/html/phpnuke/html/modules/PhaseWotd/'
alias qmake /usr/lib/qt3/bin/qmake
alias mf '/usr/local/firefox >& /dev/null &'
alias ipconfig ifconfig
# (iwconfig key HEX_KEY)
# drakconnect
alias ts  /usr/local/hcilApps/ts1.3.7/ts.sh
alias vm vr /var/log/messages
alias tm 'tail -f /var/log/messages'
alias dm 'du -sh /var/log'
alias prte 'ping 192.168.0.1'
alias pbnd 'ping bind.nd.edu'
alias ij '/usr/lib/intellij-idea-7.0/bin/idea.sh \!* nosplash >>&! /export/hda3/tmp/intellij.log &'
alias ije '/usr/local/idea-eap/bin/idea.sh >& /dev/null &'
alias ijh '/usr/lib/intellij-idea-7.0/bin/idea.sh nosplash &'
alias kij "kill `psg idea | awk '{print $2}'`"
alias du1 'du -h \!*'
alias dus1 'du -sh \!*'
alias rose /usr/local/Rational/releases/rose.2003.06.00/bin/rose
alias apms 'sync; apm --standby'
alias tm 'tail -f /var/log/messages'
alias th 'tail -f /var/log/httpd/access_log'
alias yp '/usr/local/bin/ypops.sh &'
alias tb '/usr/local/thunderbird/thunderbird &'
alias jc '/usr/local/bin/jcvs.sh >& /dev/null &'
alias pt 'pd ~pkmdev/local_dev/scm_dev/repast/doc/writeup/src/'
alias pdw 'pd ~pkmdev/local_dev/scm_dev/repast/doc/writeup/src/'
alias kyp 'kill `psg pops | head -1  |  cut -f6 -d"'" "'"`'
alias ac '/usr/local/Adobe/Acrobat7.0/bin/acroread \!* &'
alias mf '/usr/local/firefox/firefox >& /dev/null &'
alias tb '/usr/bin/thunderbird &'
alias xl 'xlock'

alias ksg 'ksysguard  >& /dev/null &'
alias nxst 'sudo /usr/NX/bin/nxserver --start &'

alias ddate 'date +%Y%m%d.%k%M:%S'

# Perforce
alias g4e 'g4 edit \!*'
alias g4ev 'set files="\!*"; g4 edit $files; vi $files'
alias g4at 'g4 addto -c \!:2 \!:1'
alias g4r 'g4 revert \!*'
alias g4sl 'g4 shortlog \!* | head -10'
alias g4nh 'g4 nothave \!*'
alias g4o "g4 opened"
alias g4og "g4o | grep \!*"
alias g4ogv "g4o | grep -v \!*"
alias g4o2 '~aiuto/bin/opened'
alias g4od "g4o | grep default | grep -v defaults"
alias g4ocl "g4o | awk '{print $5}' | sort -u"
alias g4oll "g4o | cat | grep \!* | sed -e s#//depot/g3/##g | sed -e s-#.\*--g"
alias g4ol "g4o | cat | sed -e s#//depot/g3/##g | sed -e s-#.\*--g"
alias g4pff "g4 changes -u ffaber | head -50 | grep  pending"
alias g4p "g4 pending"
alias g4pl "g4 pending | less"
alias g4ll "g4 list -c \!* | cat | sed -e s#//depot/g3/##g | sed -e s-#.\*--g"
alias g4odl "g4o | grep default | grep -v defaults | cat | sed -e s#//depot/g3/##g | sed -e s-#.\*--g"
alias g4odell "g4o | grep delete | cat | sed -e s#//depot/g3/##g | sed -e s-#.\*--g"
alias g4ondl "g4o | grep -v 'default change' | cut -d ' ' -f 5 | sort -n | uniq"
alias g4me 'g4 changes -t -u ffaber \!*'
alias bug4o 'mkdir ~/bak/`ddate`; ( echo "foreach f ( `g4ol` ) "; echo "cp \$f `ls -ltr ~/bak | tail -2 | head -1`" ; echo "end"; ) | csh'

alias p4m 'p4 monitor show'
alias p4root 'p4 info | sed -n "s,Client root: ,,p"'
alias p4r p4root
alias gaim 'gaim >& /dev/null &'

alias lS 'ls -lthSr'

alias xclock 'xclock -twentyfour -digital &'

alias fe 'foreach x ( \!* ) \
 echo compress $x \
 gzip $x \
end'

alias lynx 'lynx -accept_all_cookies'
#  xterm -T "My XTerm's Title" -n "My XTerm's Icon Title"
alias t 'echo -n "\033]0;\!*\007"'

alias lit 'li /tmp/ | tail -10'
alias et "'/usr/bin/ruby -e puts Time.at(\!^)'"
alias nxc '/usr/bin/nxclient &'
alias screen 'ssh-agent screen -h 8192 \!*'

# -n (is for each line), -e (is for exec), so this is all in one process.
alias remove-all "perl -ne chop;unlink;"
alias rns 'echo y | nc localhost 10200'

alias nxclient /usr/NX/bin/nxclient
alias pct 'pcretest'
alias lttm '/bin/ls -ltr dir1'
alias ltte '/bin/ls -ltr dir2'
alias lttat '/bin/ls -ltr dir3'
alias ltta 'set ttdate=`date "+%Y%m%d"` ; lttm >>! /tmp/tts.${ttdate}; echo "xxxxxx" >> /tmp/tts.${ttdate}; ltte >> /tmp/tts.${ttdate}; echo "xxxxxx" >> /tmp/tts.${ttdate}; lttat >> /tmp/tts.${ttdate};'

alias cleanpyc 'find . -type f -name "*.pyc" -exec rm -f {}\; -print'
alias dnst 'dig \!*; echo ""; dig txt chaos hostname.bind '

alias ff3 '/usr/bin/firefox-3.5 >& /dev/null &'

alias jsg 'jstack `jps | grep \!:1 | cut -f 1 -d " "`'
alias scr 'screen -R fromMpb'
alias sdr 'screen -dr fromMpb'

alias suffix_date 'echo `date +%Y%m%d.%H%M%S`'
alias mktdir 'mkdir `date +%Y%m%d.%H%M%S`'
alias chrome '/usr/bin/google-chrome >& /dev/null &'
alias cleanfile "tr -cd '\11\12\40-\176' \!*"
alias getclientname 'g4 client -o | egrep "^Client:" | cut -f 2'
