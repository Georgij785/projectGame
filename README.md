# projectGame
Пятибратов Георгий 10-2
проект игра
06.12.2025


bp cnelbb xfcnm cnhjxrb lkz ufkjxrb



мне нравятся как сделаны игры разработчиком майк клубника
я хочу сделать чтото похожее, так же как вдохновение игра voices of the void
то есть игра с какой то рутиной, муторной работой, и с давлением


либо же оптимистичая игра, нос большим колличеством ачивок, достижений, контента, желательно которые получаются содержательно




делать планирую на движке godot; для нго жулательно изучить язык gd script;
https://gdquest.github.io/learn-gdscript/?ref=godot-docs#course/lesson-3-standing-on-shoulders-of-giants/lesson.tres
прошёл 2 урока, скачал godot, и добавил расирение для vsc





посмотрел игры на itch.io, думаю над идеей





идея игры- буллетхел рогалик с магией и ивентами, по типо пропажи части пола, летающими врагами, и т. д. 
(похожая на мегабонк и айзека);
за сегодня прочитал базовую докуаменатцию по синтексу, классам., про функции вызывающие каждый кадр для расчёта например физики.






код с классами на gdscript




extends Node

func _ready():
	var a: Mage= Mage.new("Gengal",100)
	print(a.health)
	var b: Enemy= Enemy.new("Ork",10)
	print(a.damaged(b))
	print(a.health)

class Enemy:
	var damage: int
	var name: String
	func _init(Name: String,d:int):
		name=Name
		damage=d
	func get_damage() -> int:
		return damage
class Mage:
	var health: int
	var name: String
	func _init(Name: String,h:int):
		name=Name
		health=h
	func damaged(target: Enemy) -> String:
		health-=target.get_damage()
		return "damaged"




    вывод:
    100
    damaged
    90
