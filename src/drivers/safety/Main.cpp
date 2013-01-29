#include <iostream>
#include <fstream>
#include <vector>
#include <stdlib.h>
#include <conio.h>

using namespace std;
ifstream input("s.txt");

typedef vector <int> intVec;

vector <int> resources,available,processNumber,work,sequance,temp;
vector <bool> finish;
vector <intVec> need,max,allocation;

int main()
{
	int read,m,n;
	bool find;

	if (!input)
	{
		cerr<<"Error can not fine \"s.txt\""<<endl;
		getch();
		exit(1);
	}

	cout<<"\n\t\t\tSafety Bankers Algorithm"<<endl;
	cout<<"\t\t\t------------------------"<<endl;

	for (;;)
	{
		input>>read;
		if (read==-1)
			break;
		resources.push_back(read);
	}

	m=resources.size();

	for (;;)
	{
		temp.clear();
		input>>read;
		if (read==-1)
			break;
		processNumber.push_back(read);
		for (int j=0;j<m;j++)
		{
			input>>read;
			temp.push_back(read);
		}
		allocation.push_back(temp);
		temp.clear();

		for (j=0;j<m;j++)
		{
			input>>read;
			temp.push_back(read);
		}
		max.push_back(temp);
		temp.clear();
		if (processNumber.size()==1)
			for (j=0;j<m;j++)
			{
				input>>read;
				available.push_back(read);
			}
	}

	n=processNumber.size();

	for (int i=0;i<n;i++)
	{
		temp.clear();
		for (int j=0;j<m;j++)
		{
			temp.push_back(max[i][j]-allocation[i][j]);
		}
		need.push_back(temp);
		temp.clear();
	}

	//initialization:

	for (i=0;i<m;i++)
		work.push_back(available[i]);
	for (i=0;i<n;i++)
		finish.push_back(false);

	//safety:

	bool allFind,allFinish;
	for (;;)
	{
		allFind=false;
		for (i=0;i<n;i++)
		{
			find=false;
			if (finish[i]==false)
			{
				find=true;
				for (int j=0;j<m;j++)
					if (need[i][j]>work[j])
						find=false;
			}

			if (find)
			{
				for (int j=0;j<m;j++)
					work[j]+=allocation[i][j];
				finish[i]=true;
				sequance.push_back(i);
				allFind=true;
			}
		}
		if (!allFind)
		{
			allFinish=true;
			for (i=0;i<n;i++)
				if (!finish[i])
				{
					allFinish=false;
					break;
				}
			break;
		}
	}

	cout<<" System sanpshot:"<<endl<<endl;

	cout<<"\tProcess#\tAllocation\tMax\tAvailable"<<endl;
	cout<<"\t--------\t----------\t---\t---------"<<endl;
	for (i=0;i<n;i++)
	{
		cout<<"\t  "<<processNumber[i]<<"\t\t   ";
		for (int j=0;j<m;j++)
			cout<<allocation[i][j];
		cout<<"\t\t";
		for (j=0;j<m;j++)
			cout<<max[i][j];
		cout<<"\t   ";
		if (i==0)
		{
			for (j=0;j<m;j++)
				cout<<available[j];
		}
		cout<<endl;
	}

	cout<<"\n\t-------------------------------------------------"
	<<endl<<endl;
	if (allFinish)
	{
		cout<<"  Safe Sequance: < ";
		for (i=0;i<sequance.size();i++)
		{
			cout<<"P"<<sequance[i];
			if (i!=sequance.size()-1)
				cout<<", ";
		}
		cout<<" >"<<endl<<endl;
		cout<<"\t\t   "<<char(218);
		for (int p=1;p<30;p++)
			cout<<char(196);
		cout<<char(191)<<endl;
		cout<<"\t\t   "<<char(179)<<" The system is in SAFE state "
		<<char(179)<<endl;
		cout<<"\t\t   "<<char(192);
		for (p=1;p<30;p++)
			cout<<char(196);
		cout<<char(217)<<endl<<endl;
	}
	else
	{
		cout<<"\t\t   "<<char(218);
		for (int p=1;p<32;p++)
			cout<<char(196);
		cout<<char(191)<<endl;
		cout<<"\t\t   "<<char(179)<<" The system is in UNSAFE state "
		<<char(179)<<endl;
		cout<<"\t\t   "<<char(192);
		for (p=1;p<32;p++)
			cout<<char(196);
		cout<<char(217)<<endl<<endl;
	}

	cout<<"\n  By: Osama K. Farah"<<endl;

	getch();
	return 0;
}

