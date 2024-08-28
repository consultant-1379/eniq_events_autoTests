#!/usr/bin/python
import pexpect
import sys
import string

child = pexpect.spawn('su - root')
child.expect('assword: ')
child.sendline('shroot')
child.expect('# ')
child.sendline(string.join(sys.argv[1:]))
child.expect('# ')
print child.before
